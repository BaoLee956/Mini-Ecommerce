// Application State
let currentPage = 0;
let currentKeyword = '';
let cart = [];

// DOM Elements
const searchInput = document.getElementById('search-input');
const searchBtn = document.getElementById('search-btn');
const productsGrid = document.getElementById('products-grid');
const paginationControls = document.getElementById('pagination-controls');
const paginationInfo = document.getElementById('pagination-info');
const loadingSpinner = document.getElementById('loading-spinner');
const cartItems = document.getElementById('cart-items');
const orderBtn = document.getElementById('order-btn');

// Initialize Application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Set up event listeners
    setupEventListeners();

    // Simulate user login for demo (you can remove this in production)
    simulateUserLogin();

    // Load initial products
    loadProducts();
}

// Setup Event Listeners
function setupEventListeners() {
    // Search functionality
    searchBtn.addEventListener('click', handleSearch);
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });

    // Order button
    orderBtn.addEventListener('click', handleOrder);
}

// User Authentication Simulation
function simulateUserLogin() {
    // Simulate a logged-in user for demo purposes
    // In a real app, this would come from login flow
    const demoUser = {
        id: 1,
        username: 'demo_user',
        email: 'demo@example.com'
    };

    ApiService.setCurrentUser(demoUser);
    console.log('Demo user logged in:', demoUser);
}

// Product Search and Display
async function handleSearch() {
    currentKeyword = searchInput.value.trim();
    currentPage = 0;
    await loadProducts();
}

async function loadProducts() {
    try {
        showLoading(true);

        const response = await ApiService.searchProducts(currentKeyword, currentPage, 10);

        displayProducts(response.content);
        displayPagination(response);

    } catch (error) {
        console.error('Error loading products:', error);
        showError('Không thể tải sản phẩm. Vui lòng thử lại.');
    } finally {
        showLoading(false);
    }
}

function displayProducts(products) {
    productsGrid.innerHTML = '';

    if (products.length === 0) {
        productsGrid.innerHTML = `
            <div class="col-12 text-center">
                <p class="text-muted">Không tìm thấy sản phẩm nào.</p>
            </div>
        `;
        return;
    }

    products.forEach(product => {
        const productCard = createProductCard(product);
        productsGrid.appendChild(productCard);
    });
}

function createProductCard(product) {
    const col = document.createElement('div');
    col.className = 'col-md-6 col-lg-4';

    col.innerHTML = `
        <div class="product-card">
            <h5 class="card-title">${product.name}</h5>
            <p class="card-text text-muted">${product.description}</p>
            <div class="product-price">₫${product.price.toLocaleString('vi-VN')}</div>
            <div class="product-stock">Còn lại: ${product.stockQuantity}</div>
            <button class="btn btn-primary btn-add-cart"
                    onclick="addToCart(${product.id}, '${product.name}', ${product.price}, ${product.stockQuantity})"
                    ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                <i class="fas fa-cart-plus"></i>
                ${product.stockQuantity <= 0 ? 'Hết hàng' : 'Thêm vào giỏ'}
            </button>
        </div>
    `;

    return col;
}

// Pagination
function displayPagination(response) {
    const { pageNumber, pageSize, totalElements, totalPages, first, last } = response;

    // Update pagination info
    paginationInfo.textContent = `Hiển thị ${pageNumber * pageSize + 1}-${Math.min((pageNumber + 1) * pageSize, totalElements)} trong tổng số ${totalElements} sản phẩm`;

    // Create pagination controls
    paginationControls.innerHTML = '';

    if (totalPages <= 1) return;

    // Previous button
    if (!first) {
        const prevBtn = createPaginationButton('Trước', pageNumber - 1);
        paginationControls.appendChild(prevBtn);
    }

    // Page numbers
    const startPage = Math.max(0, pageNumber - 2);
    const endPage = Math.min(totalPages - 1, pageNumber + 2);

    for (let i = startPage; i <= endPage; i++) {
        const pageBtn = createPaginationButton((i + 1).toString(), i);
        if (i === pageNumber) {
            pageBtn.classList.add('active');
        }
        paginationControls.appendChild(pageBtn);
    }

    // Next button
    if (!last) {
        const nextBtn = createPaginationButton('Sau', pageNumber + 1);
        paginationControls.appendChild(nextBtn);
    }
}

function createPaginationButton(text, page) {
    const button = document.createElement('button');
    button.className = 'pagination-btn';
    button.textContent = text;
    button.addEventListener('click', () => {
        currentPage = page;
        loadProducts();
    });
    return button;
}

// Cart Functionality
function addToCart(productId, productName, price, maxStock) {
    const existingItem = cart.find(item => item.productId === productId);

    if (existingItem) {
        if (existingItem.quantity < maxStock) {
            existingItem.quantity += 1;
        } else {
            showError(`Không thể thêm. Chỉ còn ${maxStock} sản phẩm trong kho.`);
            return;
        }
    } else {
        cart.push({
            productId: productId,
            name: productName,
            price: price,
            quantity: 1,
            maxStock: maxStock
        });
    }

    updateCartDisplay();
    showSuccess('Đã thêm sản phẩm vào giỏ hàng!');
}

function updateCartDisplay() {
    cartItems.innerHTML = '';

    if (cart.length === 0) {
        cartItems.innerHTML = `
            <div class="empty-cart">
                <i class="fas fa-shopping-cart fa-2x mb-2"></i>
                <p>Giỏ hàng trống</p>
            </div>
        `;
        orderBtn.disabled = true;
        return;
    }

    cart.forEach((item, index) => {
        const cartItem = createCartItem(item, index);
        cartItems.appendChild(cartItem);
    });

    orderBtn.disabled = false;
}

function createCartItem(item, index) {
    const div = document.createElement('div');
    div.className = 'cart-item';

    div.innerHTML = `
        <div class="cart-item-info">
            <div class="fw-bold">${item.name}</div>
            <div class="text-muted">₫${item.price.toLocaleString('vi-VN')}</div>
        </div>
        <div class="cart-item-controls">
            <div class="quantity-controls">
                <button onclick="changeQuantity(${index}, -1)" ${item.quantity <= 1 ? 'disabled' : ''}>-</button>
                <input type="number" value="${item.quantity}" readonly min="1" max="${item.maxStock}">
                <button onclick="changeQuantity(${index}, 1)" ${item.quantity >= item.maxStock ? 'disabled' : ''}>+</button>
            </div>
            <button class="btn btn-sm btn-outline-danger ms-2" onclick="removeFromCart(${index})">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `;

    return div;
}

function changeQuantity(index, delta) {
    const item = cart[index];
    const newQuantity = item.quantity + delta;

    if (newQuantity >= 1 && newQuantity <= item.maxStock) {
        item.quantity = newQuantity;
        updateCartDisplay();
    }
}

function removeFromCart(index) {
    cart.splice(index, 1);
    updateCartDisplay();
}

// Order Submission
async function handleOrder() {
    if (cart.length === 0) {
        showError('Giỏ hàng trống!');
        return;
    }

    if (!ApiService.isAuthenticated()) {
        showError('Vui lòng đăng nhập để đặt hàng!');
        return;
    }

    try {
        // Prepare order items
        const orderItems = cart.map(item => ({
            productId: item.productId,
            quantity: item.quantity
        }));

        // Create order
        const orderResponse = await ApiService.createOrder(orderItems);

        showSuccess('Đặt hàng thành công!');
        cart = [];
        updateCartDisplay();

        console.log('Order created:', orderResponse);

    } catch (error) {
        console.error('Order error:', error);
        showError('Không thể đặt hàng: ' + error.message);
    }
}

// Utility Functions
function showLoading(show) {
    loadingSpinner.classList.toggle('d-none', !show);
}

function showError(message) {
    // Simple alert for now, can be replaced with toast notifications
    alert('Lỗi: ' + message);
}

function showSuccess(message) {
    // Simple alert for now, can be replaced with toast notifications
    alert('Thành công: ' + message);
}

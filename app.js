const getApiBase = () => document.getElementById('apiBaseUrl').value.replace(/\/$/, '');

let selectedCategoryId = null;
let editingCategoryId = null;
let editingProductId = null;

function showMessage(text, type = 'success') {
  const el = document.getElementById('message');
  el.textContent = text;
  el.className = `message ${type}`;
  el.classList.remove('hidden');
  setTimeout(() => el.classList.add('hidden'), 4000);
}

async function api(path, options = {}) {
  const url = `${getApiBase()}${path}`;
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Request failed (${res.status})`);
  }
  if (res.status === 204) return null;
  return res.json();
}

async function loadCategories() {
  const list = document.getElementById('categoryList');
  try {
    const categories = await api('/category');
    if (!categories.length) {
      list.innerHTML = '<p class="hint">No categories yet. Add one above.</p>';
      return;
    }
    list.innerHTML = categories.map(c => `
      <div class="card ${selectedCategoryId === c.categoryId ? 'selected' : ''}" data-id="${c.categoryId}">
        <h3>${escapeHtml(c.categoryName)}</h3>
        <p>${escapeHtml(c.description || 'No description')}</p>
        <p><small>ID: ${c.categoryId} · Products: ${(c.products && c.products.length) || 0}</small></p>
        <div class="card-actions" onclick="event.stopPropagation()">
          <button class="btn-edit" onclick="editCategory(${c.categoryId}, '${escapeAttr(c.categoryName)}', '${escapeAttr(c.description || '')}')">Edit</button>
          <button class="btn-danger" onclick="deleteCategory(${c.categoryId})">Delete</button>
        </div>
      </div>
    `).join('');
    list.querySelectorAll('.card').forEach(card => {
      card.addEventListener('click', () => selectCategory(Number(card.dataset.id)));
    });
    if (selectedCategoryId) {
      await loadProducts(selectedCategoryId);
    }
  } catch (err) {
    list.innerHTML = `<p class="hint error">${escapeHtml(err.message)}</p>`;
  }
}

async function selectCategory(id) {
  selectedCategoryId = id;
  editingProductId = null;
  document.getElementById('productCategoryId').value = id;
  document.getElementById('productForm').classList.remove('hidden');
  document.getElementById('productHint').classList.add('hidden');
  document.getElementById('selectedCategoryLabel').textContent = `(Category #${id})`;
  resetProductForm();
  await loadCategories();
  await loadProducts(id);
}

async function loadProducts(categoryId) {
  const list = document.getElementById('productList');
  try {
    const category = await api(`/category/${categoryId}`);
    const products = category.products || [];
    if (!products.length) {
      list.innerHTML = '<p class="hint">No products in this category.</p>';
      return;
    }
    list.innerHTML = products.map(p => `
      <div class="product-item">
        <div>
          <h4>${escapeHtml(p.productName)}</h4>
          <p>${escapeHtml(p.description || 'No description')}</p>
          <small>ID: ${p.productId}</small>
        </div>
        <div class="card-actions">
          <button class="btn-edit" onclick="editProduct(${p.productId}, '${escapeAttr(p.productName)}', '${escapeAttr(p.description || '')}')">Edit</button>
          <button class="btn-danger" onclick="deleteProduct(${categoryId}, ${p.productId})">Delete</button>
        </div>
      </div>
    `).join('');
  } catch (err) {
    list.innerHTML = `<p class="hint">${escapeHtml(err.message)}</p>`;
  }
}

function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}

function escapeAttr(str) {
  return String(str).replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

function resetCategoryForm() {
  editingCategoryId = null;
  document.getElementById('categoryId').value = '';
  document.getElementById('categoryName').value = '';
  document.getElementById('categoryDescription').value = '';
  document.getElementById('categorySubmitBtn').textContent = 'Add Category';
  document.getElementById('categoryCancelBtn').classList.add('hidden');
}

function resetProductForm() {
  editingProductId = null;
  document.getElementById('productId').value = '';
  document.getElementById('productName').value = '';
  document.getElementById('productDescription').value = '';
  document.getElementById('productSubmitBtn').textContent = 'Add Product';
  document.getElementById('productCancelBtn').classList.add('hidden');
}

function editCategory(id, name, desc) {
  editingCategoryId = id;
  document.getElementById('categoryId').value = id;
  document.getElementById('categoryName').value = name;
  document.getElementById('categoryDescription').value = desc;
  document.getElementById('categorySubmitBtn').textContent = 'Update Category';
  document.getElementById('categoryCancelBtn').classList.remove('hidden');
}

function editProduct(id, name, desc) {
  editingProductId = id;
  document.getElementById('productId').value = id;
  document.getElementById('productName').value = name;
  document.getElementById('productDescription').value = desc;
  document.getElementById('productSubmitBtn').textContent = 'Update Product';
  document.getElementById('productCancelBtn').classList.remove('hidden');
}

async function deleteCategory(id) {
  if (!confirm('Delete this category and all its products?')) return;
  try {
    await api(`/category/${id}`, { method: 'DELETE' });
    if (selectedCategoryId === id) {
      selectedCategoryId = null;
      document.getElementById('productForm').classList.add('hidden');
      document.getElementById('productHint').classList.remove('hidden');
      document.getElementById('productList').innerHTML = '';
      document.getElementById('selectedCategoryLabel').textContent = '';
    }
    showMessage('Category deleted');
    await loadCategories();
  } catch (err) {
    showMessage(err.message, 'error');
  }
}

async function deleteProduct(categoryId, productId) {
  if (!confirm('Delete this product?')) return;
  try {
    await api(`/category/${categoryId}/product/${productId}`, { method: 'DELETE' });
    showMessage('Product deleted');
    await loadProducts(categoryId);
    await loadCategories();
  } catch (err) {
    showMessage(err.message, 'error');
  }
}

document.getElementById('categoryForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const body = {
    categoryName: document.getElementById('categoryName').value.trim(),
    description: document.getElementById('categoryDescription').value.trim() || null
  };
  try {
    if (editingCategoryId) {
      await api(`/category/${editingCategoryId}`, { method: 'PUT', body: JSON.stringify(body) });
      showMessage('Category updated');
    } else {
      await api('/category', { method: 'POST', body: JSON.stringify(body) });
      showMessage('Category created');
    }
    resetCategoryForm();
    await loadCategories();
  } catch (err) {
    showMessage(err.message, 'error');
  }
});

document.getElementById('productForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const categoryId = document.getElementById('productCategoryId').value;
  if (!categoryId) {
    showMessage('Select a category first', 'error');
    return;
  }
  const body = {
    productName: document.getElementById('productName').value.trim(),
    description: document.getElementById('productDescription').value.trim() || null
  };
  try {
    if (editingProductId) {
      await api(`/product/${editingProductId}`, { method: 'PUT', body: JSON.stringify(body) });
      showMessage('Product updated');
    } else {
      await api(`/product/${categoryId}`, { method: 'POST', body: JSON.stringify(body) });
      showMessage('Product created');
    }
    resetProductForm();
    await loadProducts(categoryId);
    await loadCategories();
  } catch (err) {
    showMessage(err.message, 'error');
  }
});

document.getElementById('categoryCancelBtn').addEventListener('click', resetCategoryForm);
document.getElementById('productCancelBtn').addEventListener('click', resetProductForm);
document.getElementById('refreshBtn').addEventListener('click', loadCategories);

loadCategories();

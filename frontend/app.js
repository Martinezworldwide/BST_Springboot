(function () {
  // Allow override via query: ?api=https://your-app.onrender.com
  const params = new URLSearchParams(location.search);
  const API_BASE = params.get('api') || window.API_BASE || '';
  const api = (path, options) => fetch(API_BASE + path, { ...options, headers: { 'Content-Type': 'application/json', ...options?.headers } });

  const el = (id) => document.getElementById(id);
  const setResult = (id, html, isEmpty) => {
    const r = el(id);
    if (!r) return;
    r.innerHTML = html;
    r.classList.toggle('empty', !!isEmpty);
  };

  // Show API base and check health
  el('api-base').textContent = 'API: ' + (API_BASE || '(not set)') + ' ';
  api('/actuator/health').then(res => {
    el('api-status-text').textContent = res.ok ? 'Connected' : 'Error ' + res.status;
    el('api-status-text').className = res.ok ? 'ok' : 'fail';
  }).catch(() => {
    el('api-status-text').textContent = 'Offline (start backend or set API_BASE)';
    el('api-status-text').className = 'fail';
  });

  // Insert
  el('form-insert').addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
      transactionId: el('insert-id').value.trim(),
      amount: parseFloat(el('insert-amount').value),
      reason: el('insert-reason').value.trim() || null
    };
    setResult('insert-result', 'Sending…', false);
    try {
      const res = await api('/api/flagged-transactions', { method: 'POST', body: JSON.stringify(payload) });
      const data = await res.json().catch(() => ({}));
      if (res.ok && data.success) {
        setResult('insert-result', 'Inserted. Tree size: ' + (data.size ?? '—'), false);
        loadList();
      } else {
        setResult('insert-result', 'Error: ' + (data.message || res.status), false);
      }
    } catch (err) {
      setResult('insert-result', 'Request failed: ' + err.message, false);
    }
  });

  // Search
  el('form-search').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = el('search-id').value.trim();
    setResult('search-result', 'Searching…', false);
    try {
      const res = await api('/api/flagged-transactions/search?transactionId=' + encodeURIComponent(id));
      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        setResult('search-result', 'Error: ' + res.status, false);
        return;
      }
      let html = 'BST comparisons: ' + (data.bstComparisons ?? '—') + '\nList comparisons: ' + (data.listComparisons ?? '—') + '\nList size: ' + (data.listSize ?? '—');
      if (data.efficiencyNote) html += '\n\n' + data.efficiencyNote;
      if (data.found && data.transaction) {
        html += '\n\nTransaction: ' + data.transaction.transactionId + ', amount: ' + data.transaction.amount + (data.transaction.reason ? ', reason: ' + data.transaction.reason : '');
      } else {
        html += '\n\nNot found.';
      }
      setResult('search-result', html, false);
    } catch (err) {
      setResult('search-result', 'Request failed: ' + err.message, false);
    }
  });

  // Delete
  el('form-delete').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = el('delete-id').value.trim();
    setResult('delete-result', 'Deleting…', false);
    try {
      const res = await api('/api/flagged-transactions/' + encodeURIComponent(id), { method: 'DELETE' });
      const data = await res.json().catch(() => ({}));
      let html = (data.deleted ? 'Deleted.' : 'Not found or error.') + '\nNode type: ' + (data.nodeType || '—');
      if (data.impactExplanation) html += '\n\n' + data.impactExplanation;
      setResult('delete-result', html, false);
      if (data.deleted) loadList();
    } catch (err) {
      setResult('delete-result', 'Request failed: ' + err.message, false);
    }
  });

  // List + size
  function loadList() {
    api('/api/flagged-transactions').then(res => res.json()).then(list => {
      el('tree-size').textContent = list.length + ' transaction(s).';
      if (list.length === 0) {
        setResult('list-result', 'No flagged transactions. Use Insert to add.', true);
        return;
      }
      const ul = list.map(t => '<li>' + t.transactionId + ' — ' + t.amount + (t.reason ? ' — ' + t.reason : '') + '</li>').join('');
      setResult('list-result', '<ul>' + ul + '</ul>', false);
    }).catch(() => {
      el('tree-size').textContent = '';
      setResult('list-result', 'Could not load list.', false);
    });
  }

  el('btn-refresh').addEventListener('click', loadList);

  // Docs
  api('/api/docs/bst-explanation').then(res => res.json()).then(data => {
    let html = '';
    if (data.insertion) {
      html += '<h3>Insertion</h3><p>' + (data.insertion.description || '') + '</p><p>' + (data.insertion.behavior || '') + '</p>';
    }
    if (data.search) {
      html += '<h3>Search (BST vs list)</h3><p>' + (data.search.description || '') + '</p><p>BST: ' + (data.search.bstEfficiency || '') + '</p><p>List: ' + (data.search.listEfficiency || '') + '</p><p>' + (data.search.comparison || '') + '</p>';
    }
    if (data.deletion) {
      html += '<h3>Deletion</h3><p>' + (data.deletion.description || '') + '</p>';
      html += '<p><strong>Leaf:</strong> ' + (data.deletion.leafNode || '') + '</p>';
      html += '<p><strong>One child:</strong> ' + (data.deletion.oneChild || '') + '</p>';
      html += '<p><strong>Two children:</strong> ' + (data.deletion.twoChildren || '') + '</p>';
    }
    if (data.fraudSystemImpact) html += '<p>' + data.fraudSystemImpact + '</p>';
    setResult('docs-result', html || 'No documentation loaded.', !html);
  }).catch(() => setResult('docs-result', 'Could not load documentation.', false));

  loadList();
})();

async function callPattern(endpoint, requestBody) {
    const responseDiv = document.getElementById('response');
    const loadingDiv = document.getElementById('loading');
    const submitBtn = document.getElementById('submit-btn');
    
    try {
        // Show loading state
        loadingDiv.style.display = 'block';
        responseDiv.innerHTML = '';
        submitBtn.disabled = true;
        
        // Call API
        const response = await fetch(`/api/gpt5/${endpoint}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(requestBody)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        
        // Display response
        displayResponse(data.result || data.response || JSON.stringify(data));
        
    } catch (error) {
        displayError('Failed to get response: ' + error.message);
    } finally {
        loadingDiv.style.display = 'none';
        submitBtn.disabled = false;
    }
}

function displayResponse(text) {
    const responseDiv = document.getElementById('response');
    const escapedText = escapeHtml(text);
    responseDiv.innerHTML = `
        <div class="response-card">
            <div class="response-header">
                <span class="response-label">AI Response</span>
                <button onclick="copyToClipboard(\`${escapedText}\`)" class="btn-copy">📋 Copy</button>
            </div>
            <div class="response-body">
                <pre>${escapedText}</pre>
            </div>
            <div class="response-footer">
                <small>Generated at ${new Date().toLocaleTimeString()}</small>
            </div>
        </div>
    `;
}

function displayError(message) {
    const responseDiv = document.getElementById('response');
    responseDiv.innerHTML = `
        <div class="error-message">
            <strong>Error:</strong> ${escapeHtml(message)}
        </div>
    `;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function copyToClipboard(text) {
    // Unescape HTML entities first
    const textarea = document.createElement('textarea');
    textarea.innerHTML = text;
    const plainText = textarea.value;
    
    navigator.clipboard.writeText(plainText).then(() => {
        alert('Copied to clipboard!');
    }).catch(err => {
        console.error('Failed to copy:', err);
    });
}

function setExample(text) {
    // Try to find the main input field by common IDs first
    const input = document.getElementById('code') || 
                  document.getElementById('task') || 
                  document.getElementById('problem') ||
                  document.getElementById('requirement') ||
                  document.getElementById('question') ||
                  document.getElementById('topic') ||
                  document.querySelector('textarea:not([readonly]), input[type="text"]:not([readonly])');
    
    if (input) {
        input.value = text;
        input.focus();
    }
}

/**
 * Generic streaming function for all patterns.
 * Calls POST /api/gpt5/{endpoint}/stream with SSE and renders tokens in real-time.
 */
function callPatternStreaming(endpoint, requestBody) {
    const responseDiv = document.getElementById('response');
    const loadingDiv = document.getElementById('loading');
    const submitBtn = document.getElementById('submit-btn');

    loadingDiv.style.display = 'block';
    submitBtn.disabled = true;
    responseDiv.innerHTML = `
        <div class="response-card">
            <div class="response-header">
                <span class="response-label">\u{1F534} Streaming AI Response</span>
                <span id="token-counter" style="font-size:12px; color:#888;">0 tokens</span>
            </div>
            <div class="response-body">
                <pre id="stream-output" style="white-space: pre-wrap;"></pre>
            </div>
            <div class="response-footer">
                <small id="stream-status">Connecting...</small>
            </div>
        </div>
    `;

    const outputEl = document.getElementById('stream-output');
    const statusEl = document.getElementById('stream-status');
    const counterEl = document.getElementById('token-counter');
    let tokenCount = 0;
    let sseBuffer = '';

    fetch(`/api/gpt5/${endpoint}/stream`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestBody)
    }).then(response => {
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        loadingDiv.style.display = 'none';
        statusEl.textContent = 'Streaming...';

        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let streamDone = false;

        function processSSEEvents(text) {
            sseBuffer += text;
            const events = sseBuffer.split('\n\n');
            sseBuffer = events.pop() || '';

            for (const event of events) {
                if (!event.trim()) continue;
                const lines = event.split('\n');
                let eventName = '';
                const dataLines = [];

                for (const line of lines) {
                    if (line.startsWith('event:')) {
                        eventName = line.substring(6).trim();
                    } else if (line.startsWith('data:')) {
                        dataLines.push(line.substring(5));
                    }
                }

                const data = dataLines.join('\n');

                if (eventName === 'done' || data.trim() === '[DONE]') {
                    statusEl.textContent = `Complete at ${new Date().toLocaleTimeString()} \u2014 ${tokenCount} tokens`;
                    submitBtn.disabled = false;
                    streamDone = true;
                    return;
                }

                if (eventName === 'error') {
                    statusEl.textContent = 'Error: ' + data;
                    submitBtn.disabled = false;
                    streamDone = true;
                    return;
                }

                if (eventName === 'token' && dataLines.length > 0) {
                    outputEl.textContent += data;
                    tokenCount++;
                    counterEl.textContent = tokenCount + ' tokens';
                }
            }
        }

        function read() {
            reader.read().then(({ done, value }) => {
                if (done || streamDone) {
                    if (!streamDone) {
                        statusEl.textContent = `Complete at ${new Date().toLocaleTimeString()} \u2014 ${tokenCount} tokens`;
                        submitBtn.disabled = false;
                    }
                    return;
                }
                const text = decoder.decode(value, { stream: true });
                processSSEEvents(text);
                if (!streamDone) read();
            }).catch(err => {
                statusEl.textContent = 'Error: ' + err.message;
                submitBtn.disabled = false;
            });
        }
        read();
    }).catch(err => {
        loadingDiv.style.display = 'none';
        responseDiv.innerHTML = `<div class="error-message"><strong>Error:</strong> ${err.message}</div>`;
        submitBtn.disabled = false;
    });
}

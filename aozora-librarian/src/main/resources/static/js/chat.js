/* ============================================
   青空図書館 チャットUI 動作スクリプト
   ============================================ */

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('chatForm');
    const button = document.getElementById('sendButton');
    const textarea = document.getElementById('messageInput');

    // 送信中のUI制御
    form.addEventListener('submit', () => {
        button.disabled = true;
        button.textContent = '司書が考えています…';
    });

    // ページ表示時に履歴の最下部までスクロール + 入力欄にフォーカス
    window.scrollTo(0, document.body.scrollHeight);
    textarea.focus();
});
document.addEventListener('DOMContentLoaded', () => {
    const registerButton = document.getElementById('registerButton');
    registerButton.addEventListener('click', handleRegister);
});

async function handleRegister(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const requestBody = JSON.stringify({username, password});
    console.log(`Sending: ${requestBody}}`)

    try {
        const response = await fetch('http://link2img.net/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: requestBody
        });

        if (!response.ok) {
            throw new Error('Failed to register.');
        }

        const data = await response.json();
        console.log(data.message);

        // If the register is successful, set the logged_in state and save the authorization token.
        window.localStorage.setItem('authToken', data.authToken);
        window.localStorage.setItem('loggedIn', "true");
        location.href='../index.html'

    } catch (error) {
        console.error('Error during register:', error);
    }
}
document.addEventListener('DOMContentLoaded', () => {
    const loginButton = document.getElementById('loginButton');
    loginButton.addEventListener('click', handleLogin);
});

async function handleLogin(event) {
    const errorReport = document.getElementById('errorReport');
    errorReport.style.display = "none";

    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const requestBody = JSON.stringify({username, password});
    console.log(`Sending: ${requestBody}}`)

    try {
        const response = await fetch('https://link2img.net/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: requestBody
        });

        if (!response.ok) {
            errorReport.innerHTML = "Error, incorrect credentials."
            errorReport.style.display = "block";
            throw new Error('Failed to log in.');
        }

        const data = await response.json();
        //console.log(`Saving auth token: ${data.authToken}`);

        // If the login is successful, set the logged_in state and save the authorization token.
        //window.localStorage.setItem('authToken', data.authToken);
        window.localStorage.setItem('loggedIn', "true");
        location.href='../'
    } catch (error) {
        console.error('Error during login:', error);
    }
}
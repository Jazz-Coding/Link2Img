const config = {
    uploadUrl: 'http://link2img.net/upload'
};

document.addEventListener('DOMContentLoaded', () => {
    let cachedImageFile = null;
    let cachedLink = "";


    let visibilityPublic = window.localStorage.getItem("defaultVisibility")==="true";
    let logged_in = window.localStorage.getItem("loggedIn")==="true";

    const imageUploadBox = document.getElementById('imageUploadBox');
    const fileElem = document.getElementById('fileElem');
    const preview = document.getElementById('preview');
    const uploadButton = document.getElementById('uploadButton');
    const privateButton = document.getElementById('privateButton');
    const publicButton = document.getElementById('publicButton');
    const copyLinkButton = document.getElementById('copyLinkButton');
    const newTabButton = document.getElementById('newTabButton');

    const linkBoxContainer = document.getElementById('linkBoxContainer');
    const linkBox = document.getElementById('linkBox');
    const preUploadUI = document.getElementById('preUploadUI');
    const visibilityDescription = document.getElementById('visibilityDescription');
    const uploadStatus = document.getElementById('uploadStatus');
    const uploadPlus = document.getElementById('uploadPlus');

    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');
    const registerButton = document.getElementById('registerButton');

    // Initial UI setup
    initVisibility(visibilityPublic);
    updateLoggedInUI();
    /*privateButton.classList.add("disable");
    setImagePrivate();
    updateLoggedInUI();*/

    // Event Listeners
    /*loginButton.addEventListener('click', handleLogin);*/
    logoutButton.addEventListener('click', handleLogout);
    imageUploadBox.addEventListener('dragover', handleDragOver);
    imageUploadBox.addEventListener('dragleave', handleDragLeave);
    imageUploadBox.addEventListener('drop', handleDrop);
    imageUploadBox.addEventListener('click', () => fileElem.click());
    fileElem.addEventListener('change', handleFileChange);
    uploadButton.addEventListener('click', uploadFile);
    copyLinkButton.addEventListener('click', copyLink);
    newTabButton.addEventListener('click', openInNewTab);
    privateButton.addEventListener('click', setImagePrivate);
    publicButton.addEventListener('click', setImagePublic);

    function handleLogout() {
        window.localStorage.setItem('authToken', "");
        window.localStorage.setItem('loggedIn', "false");
        location.href='index.html';
        location.reload();
    }

    function handleDragOver(event) {
        event.preventDefault();
        imageUploadBox.classList.add('dragover');
    }

    function handleDragLeave() {
        imageUploadBox.classList.remove('dragover');
    }

    function handleDrop(event) {
        event.preventDefault();
        imageUploadBox.classList.remove('dragover');
        const files = event.dataTransfer.files;
        handleFiles(files);
    }

    function handleFileChange() {
        const files = fileElem.files;
        handleFiles(files);
    }

    function handleFiles(files) {
        if (files.length > 0) {
            cachedImageFile = files[0];
            previewImage();
        }
    }

    function previewImage() {
        const file = cachedImageFile;
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            preview.src = reader.result; // Set the image source to the uploaded one
            imageUploadBox.style.width = 'auto'; // Shrink the upload box to the image's proportions.
            imageUploadBox.style.height = 'auto';
            preview.style.display = 'block'; // Show the image
            uploadPlus.style.display = 'none'; // Hide the +
            uploadButton.style.display = 'inline'; // Show the upload button.
        };
    }

    async function uploadFile() {
        if (!cachedImageFile) {
            alert('No file selected!');
            return;
        }

        const formData = new FormData();
        formData.append('file', cachedImageFile);
        formData.append('isPublic', visibilityPublic);

        const authToken = logged_in ? window.localStorage.getItem("authToken") : "";

        try {
            const response = await fetch(config.uploadUrl, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + authToken
                },
                body: formData
            });
            const data = await response.text();
            cachedLink = data;
            preUploadUI.style.display = 'none';
            linkBoxContainer.style.display = 'flex';
            linkBox.value = cachedLink;
        } catch (error) {
            cachedLink = "";
            console.error('Error uploading file:', error);
            uploadStatus.textContent = 'Error uploading file';
        }
    }

    function copyLink() {
        linkBox.focus();
        linkBox.select();
        navigator.clipboard.writeText(linkBox.value)
            .then(() => {
                // Optional: Provide user feedback, e.g., change button text to "Copied!"
                copyLinkButton.innerHTML = 'Copied';
            })
            .catch(err => console.error('Error copying link:', err));
    }

    function openInNewTab() {
        if (cachedLink.length > 0) {
            window.open(cachedLink, '_blank').focus();
        }
    }


    function initVisibility(isPublicDefaultValue){
        if(isPublicDefaultValue){
            // Toggle off the private button.
            privateButton.classList.toggle("disable");

            // Set the description to public.
            visibilityDescription.innerHTML = '<strong>Public</strong> images are visible to anyone who has the link.';
        } else {
            // Toggle off the public button.
            publicButton.classList.toggle("disable");

            // Set the description to private.
            visibilityDescription.innerHTML = '<strong>Private</strong> images can only be seen by you.';
        }
    }


    function saveVisibilityPreference(){
        window.localStorage.setItem("defaultVisibility",visibilityPublic);
    }

    function setImagePrivate() {
        if (visibilityPublic) {
            visibilityPublic = false;
            visibilityDescription.innerHTML = '<strong>Private</strong> images can only be seen by you.';
            privateButton.classList.toggle("disable");
            publicButton.classList.toggle("disable");

            // Save this as a user preference.
            saveVisibilityPreference();
        }
    }

    function setImagePublic() {
        if (!visibilityPublic) {
            visibilityPublic = true;
            visibilityDescription.innerHTML = '<strong>Public</strong> images are visible to anyone who has the link.';
            privateButton.classList.toggle("disable");
            publicButton.classList.toggle("disable");

            // Save this as a user preference.
            saveVisibilityPreference();
        }
    }

    function updateLoggedInUI() {
        if (logged_in) {
            // Show Logout
            loginButton.style.display = 'none';
            registerButton.style.display = 'none';
            logoutButton.style.display = 'inline';
        } else {
            // Show Login & Register
            loginButton.style.display = 'inline';
            registerButton.style.display = 'inline';
            logoutButton.style.display = 'none';
        }
    }
});

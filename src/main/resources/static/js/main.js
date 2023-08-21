$(document).click(function() {
    $('#loginForm').submit(function(e) {
        e.preventDefault(); // Prevent form submission
        const submitButton = $('#loginBtn');

        setTimeout(function() {
            submitButton.html(
                '<i class="fa fa-circle-o-notch fa-spin"></i> loading...'
            );

            // Submit the form
            $('#loginForm')[0].submit();
        }, 1000);
    });

    $('#signUpForm').submit(function(e) {
        e.preventDefault(); // Prevent form submission
        const submitButton = $('#signUpBtn');

        setTimeout(function() {
            submitButton.html(
                '<i class="fa fa-circle-o-notch fa-spin"></i> creating...'
            );

            // Submit the form
            $('#signUpForm')[0].submit();
        }, 1000);
    });
});
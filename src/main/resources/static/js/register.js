$(function () {
    const $registerForm = $('#registerForm');
    $.validator.addMethod('customEmail', function (value, element) {
        return this.optional(element) || /^([a-zA-Z0-9_.\-])+@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value)
    })
    if ($registerForm.length) {
        $registerForm.validate({
            rules: {
                name: {
                    required: true,
                    minlength: 3
                },
                email: {
                    required: true,
                    customEmail: true
                },
                password: {
                    required: true,
                    minlength: 4,
                    maxlength: 15
                },
                confirmPassword: {
                    required: true,
                    equalTo: '#password'
                }
            },
            messages: {
                name: {
                    required: 'Please enter the name for the user',
                    minlength: 'Name should not be shorter than 3 characters'
                },
                email: {
                    required: 'Please enter the email',
                    customEmail: 'Please enter valid email address'
                },
                password: {
                    required: 'Please enter password',
                    minlength: 'Password should not be shorter than 4 characters',
                    maxlength: 'Password should not be longer than 15 characters'
                },
                confirmPassword: {
                    required: 'Please confirm password',
                    equalTo: 'This field should contain exactly the same password'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        })
    }
})
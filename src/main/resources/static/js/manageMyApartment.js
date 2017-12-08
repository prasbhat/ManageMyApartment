$(document).ready(function(){
    //Login Page jquery js
    $("#loginButton").prop('disabled', true);
    $("form input").keyup(function() {

        if($(this).val() != '') {
            $("#loginButton").prop('disabled', false);
        } else {
            $("#loginButton").prop('disabled', true);
        }
    });

    //Register User jquery js
    $("#residentStatus").change(function(){
        if($("#residentStatus").val() == 'TENANT') {
            $("#OwnerSection").show();
        } else {
            $("#OwnerSection").hide();
        }
    });

    //Reset Password Page
    $("#resetPassword").prop('disabled', true);
    $("form input").keyup(function() {

        if($(this).val() != '') {
            $("#resetPassword").prop('disabled', false);
        } else {
            $("#resetPassword").prop('disabled', true);
        }
    });

    $("#resetPassword").click(function(){
        if( !isEmail($("#emailAddr").val())) {
            $(".error-message").show().html("Please Enter Valid Email Address");
        } else if ($("#new_password").val() != $("#confirm_password").val() ){
            $(".error-message").show().html("Enter Confirm Password Same as Password");
        } else {
            $("#resetPasswordForm").submit();
         }
    });

    //Transaction Page Jquert JS
    //Projected Expense Page JQuery JS
    $("#add-row").click(function(){
        $("#add_row_table").toggle();
    });

    $("#history-details").click(function(){
        $("#section-details").toggle();
        $("#add-row").toggle();
        $("#add_row_table").hide();
        $("#display-total").toggle();
        $("#section-hist-details").toggle();
        $("#current-link-details").toggle();
        $("#history-details").toggle();
    });

    $("#current-link-details").click(function(){
        $("#section-details").toggle();
        $("#add-row").toggle();
        $("#add_row_table").hide();
        $("#display-total").toggle();
        $("#section-hist-details").toggle();
        $("#current-link-details").toggle();
        $("#history-details").toggle();
    });
});


function isEmail(email) {
  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return regex.test(email);
}

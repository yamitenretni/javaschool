$(document).ready(function () {
    var $incompatibleOptions = $("[name='incompatible_options[]']");
    var $mandatoryOptions = $("[name='mandatory_options[]']");

    $incompatibleOptions.add($mandatoryOptions).on("change", function () {
        var $option = $(this);
        var optionValue = $option.val();
        var optionChecked = $option.prop('checked');

        var $mirrorOptions = $option.is($incompatibleOptions)
                ? $mandatoryOptions
                : $incompatibleOptions;
        var $mirrorOption = $mirrorOptions.filter("[value=" + optionValue + "]");

        $mirrorOption.prop('checked', false);
        $mirrorOption.prop('disabled', optionChecked);
    });
});

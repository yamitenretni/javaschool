$(document).ready(function() {
    var $contractTariff = $("#contractTariff");
    var $availableOptions = $("#availableOptions");

    $contractTariff.on("change", onTariffChange);

    function onTariffChange() {
        var tariffId = $contractTariff.val();
        var ajaxUrl = "/tariffs/options/" + tariffId;

        $.get(ajaxUrl, function(data) {
            $availableOptions.empty();
            data.forEach(function(option) {
                var text = [
                    option.name,
                    option.connectionCost,
                    'once +',
                    option.monthlyCost,
                    'every month'
                ].join(' ');

                var $cb = $('<input/>', {
                    type: 'checkbox',
                    name: 'selectedOptions[]',
                    value: option.id
                });
                var $span = $('<span/>').text(text);
                var $label = $('<label/>');
                var $div = $('<div/>', {
                    class: 'checkbox'
                });

                $cb.appendTo($label);
                $span.appendTo($label);
                $label.appendTo($div);
                $div.appendTo($availableOptions);
            });
        });

    }
});

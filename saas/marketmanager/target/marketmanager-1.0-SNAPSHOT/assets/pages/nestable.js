
/**
* Theme: Minton Admin Template
* Author: Coderthemes
* Nestable Component
*/

!function($) {
    "use strict";

    var Nestable = function() {};

    Nestable.prototype.updateOutput = function (e) {
        var list = e.length ? e : $(e.target),
            output = list.data('output');
        if (window.JSON) {
            output.val(window.JSON.stringify(list.nestable('serialize'))); //, null, 2));
        } else {
            output.val('JSON browser support required for this demo.');
        }
    },
    //init
    Nestable.prototype.init = function() {
        // activate Nestable for list 1
        $('#item_type_sort').nestable({
            group: 1
        }).on('change', this.updateOutput);

        // activate Nestable for list 2
        //$('#nestable_list_2').nestable({
        //    group: 1
        //}).on('change', this.updateOutput);

        // output initial serialised data
        //this.updateOutput($('#item_type_sort').data('output', $('#item_type_sort_output')));
        //this.updateOutput($('#nestable_list_2').data('output', $('#nestable_list_2_output')));

        // $('#nestable_list_menu').on('click', function (e) {
        //     var target = $(e.target),
        //         action = target.data('action');
        //     if (action === 'expand-all') {
        //         $('.dd').nestable('expandAll');
        //     }
        //     if (action === 'collapse-all') {
        //         $('.dd').nestable('collapseAll');
        //     }
        // });

        //$('#nestable_list_3').nestable();
    },
    //init
    $.Nestable = new Nestable, $.Nestable.Constructor = Nestable
}(window.jQuery),

//initializing 
function($) {
    "use strict";
    $.Nestable.init()
}(window.jQuery);

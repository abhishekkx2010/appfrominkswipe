<?php
defined('BASEPATH') OR exit('No direct script access allowed');
$is_create = $this->uri->segment(3) == "create" ? TRUE : FALSE;
?>
<style>
    .btn.btn-default:hover{color: #333;}
    .span_error{color: #FF0000;}
    span.span_error p{color: #FF0000;}
</style>
<div class="content-wrapper">
    <section class="content-header">
        <?php echo $pagetitle; ?>
        <?php echo $breadcrumb; ?>
        <div class="clearfix"></div>
    </section>
    <section class="content">
        <div class="row">
            <div class="col-md-12">
               <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title"><?php echo $is_create ? lang('') : lang(''); ?>  
                    </h3>
                </div>
                <div class="box-body">
                    <div class="col-md-12">
                        <?php echo form_open_multipart(current_url(), array('class' => 'form-horizontal', 'id' => 'form_action_society')); ?>
                        <div class="form-group">
                            <div class="col-md-6">
                                <div class="row">
                                    <div class="col-md-4">
                                        <?php echo lang('society_name', 'society_name', array('class' => ' control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-8">
                                        <input type="text" class="form-control modal_text_width" name="society_name" id="society_name" value="<?php echo $society_name?>" placeholder="Society Name" maxlength="100" ><br>
                                        <span class="span_error"><?php echo form_error('society_name'); ?></span>
                                    </div>
                                    <div class="col-md-4">
                                        <?php echo lang('society_address', 'society_address', array('class' => ' control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-8">
                                        <textarea  class="form-control modal_text_width" name="society_address" id="society_address" value="" placeholder="Society Address" maxlength="100" style=" resize:vertical ;"><?php echo $society_address; ?></textarea><br>
                                        <span class="span_error"><?php echo form_error('society_address'); ?></span>
                                    </div>
                                    <div class="col-md-4">
                                        <?php echo lang('common_landmark', 'common_landmark', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>  
                                    <div class="col-md-8">
                                        <input type="text"  class="form-control modal_text_width" name="society_landmark" id="society_landmark" value="<?php echo $society_landmark; ?>" placeholder="Society Landmark" maxlength="50"><br>
                                        <span class="span_error"><?php echo form_error('society_landmark'); ?></span>
                                    </div>
                                    <div class="col-md-4">
                                        <?php echo lang('common_area', 'common_area', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>  
                                    <div class="col-md-8">
                                        <input type="text"  class="form-control modal_text_width" name="society_area" id="society_area" value="<?php echo $society_area; ?>" placeholder="Society Area" maxlength="100"><br>
                                        <span class="span_error"><?php echo form_error('society_area'); ?></span>
                                    </div>
                                    <div class="col-md-4">
                                        <?php echo lang('common_state', 'common_state', array('class' => ' control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-8">
                                        <select  class="form-control modal_text_width" name="society_state" id="society_state">
                                            <?php
                                            foreach ($state_list as $key => $value) {
                                                $state = strtolower($value['state_name']);
                                                $state[0] = strtoupper($state[0]);
                                                if(!empty($state_id) && $value['state_id'] == $state_id){ ?>
                                                    <option value="<?php  echo $value['state_id'];  ?>" selected="selected">
                                                        <?php  echo $state; unset($value);}?>
                                                    </option>
                                                    <?php if(isset($value))
                                                    {?>
                                                        <option value="<?php echo $value['state_id']; ?>">
                                                            <?php echo $state; ?>
                                                        </option>                                      
                                                        <?php  } } ?>
                                                    </select><br>
                                                    <span class="span_error"><?php echo form_error('society_state'); ?></span>
                                                </div>
                                                <div class="col-md-4">
                                                    <?php echo lang('common_city', 'common_city', array('class' => 'control-label-coupon')); ?>
                                                </div>
                                                
                                                <div class="col-md-8">
                                                    <select  class="form-control modal_text_width" name="society_city" id="society_city">
                                                        <?php if(!empty($society_city)){
                                                            $city = strtolower($society_city);
                                                            $city[0] = strtoupper($city[0])
                                                            ?>
                                                            <option value="<?php echo $city; ?>">
                                                                <?php echo ucfirst($city); ?>
                                                            </option>
                                                            <?php }?>
                                                        </select><br>
                                                        <span class="span_error"><?php echo form_error('society_city'); ?></span>
                                                    </div>
                                                    <div class="col-md-4">
                                                       <?php echo lang('common_pincode', 'common_pincode', array('class' => 'control-label-coupon')); ?>
                                                       <span class="span_error">*</span>  
                                                    </div>
                                                    <div class="col-md-8">
                                                        <input type="text"  class="form-control modal_text_width" name="society_pincode" id="society_pincode" value="<?php echo $society_pincode; ?>" placeholder="Society Pincode" maxlength="6"><br>
                                                        <span class="span_error"><?php echo form_error('society_pincode'); ?></span>
                                                    </div>
                                                    <div class="col-md-4"><?php echo lang('created_by', 'created_by', array('class' => 'control-label-coupon')); ?>
                                                    <span class="span_error">*</span>  
                                                    </div>
                                                    
                                                    <div class="col-md-8">
                                                        <input type="text"  class="form-control modal_text_width" name="created_by" id="created_by" value="<?php echo $created_by; ?>" placeholder="Sociey Created By" maxlength="100"><br>
                                                        <span class="span_error"><?php echo form_error('created_by'); ?></span>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <?php echo lang('society_post_flag', 'society_post_flag', array('class' => 'control-label-coupon')); ?>
                                                    </div>
                                                    
                                                    <div class="col-md-8 mt5">
                                                        <select class="form-control modal_text_width" name="society_post_flag" id="society_post_flag">
                                                            <option value="1" <?php echo $status=="1"?"selected":""; ?>>Yes</option>
                                                            <option value="0" <?php echo $status=="0"?"selected":""; ?>>No</option>
                                                        </select><br>

                                                    </div>
                                                    <div id="xxx">
                                                    <div class="col-md-4">
                                                       <?php echo lang('society_post', 'society_post', array('class' => 'control-label-coupon')); ?> 
                                                       <span class="span_error">*</span>
                                                    </div>
                                                    
                                                    <div class="col-md-8">
                                                        <input type="text"  class="form-control modal_text_width" name="society_post" id="society_post" value="<?php echo $post; ?>" placeholder="Society Post" maxlength="50"><br>
                                                        <span class="span_error"><?php echo form_error('society_post'); ?></span>
                                                    </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                         <?php echo lang('society_image', 'society_image', array('class' => 'control-label-coupon')); ?>
                                                    </div>
                                                    <div class="col-md-8 mt5">
                                                        <input type="file" name="society_image" id="society_image" accept=".jpg,.jpeg,.png,.bmp" />
                                                        <span>( Upload image of Size 800px * 600px )</span>
                                                        <span class="span_error"><?php echo form_error('society_image'); ?></span>&nbsp;&nbsp;
                                                        <?php if(!empty($society_image)){?>
                                                            <img src="<?php echo $this->config->item('uploaded_image_url').'images/society_images/'.$society_image;?>" style="width:104px;height:78px;" class="img-responsive">
                                                            <?php } ?>
                                                            &nbsp;&nbsp;
                                                        </div>
                                                        <div class="col-md-4">
                                                            <?php echo lang('common_status', 'common_status', array('class' => 'control-label-coupon')); ?>
                                                        </div>
                                                        
                                                        <div class="col-md-8 mt5">
                                                            <select class="form-control modal_text_width" name="status" id="status">
                                                                <option value="1" <?php echo $status=="1"?"selected":""; ?>>Active</option>
                                                                <option value="0" <?php echo $status=="0"?"selected":""; ?>>Inactive</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>                                                         
                                            </div>
                                        </div>
                                    </div>
                                    <div class="box-footer">
                                        <?php $action = $is_create ? "Submit" : "Update"; ?>
                                        <div class="col-sm-12">
                                            <div class="btn-group societies_btn pull-right">
                                                <?php echo form_button(array('type' => 'submit', 'class' => 'btn btn-success btn-flat color_add', 'content' => $action)); ?>
                                                <?php echo anchor('admin/society', lang('actions_cancel'), array('class' => 'btn btn-default btn-flat color_close')); ?>
                                            </div>
                                        </div>
                                    </div>
                                    <?php echo form_close();?>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
<script type="text/javascript">
    $(document).ready(function () {

        $.validator.addMethod("number", function(value, element) {
        return this.optional(element) || value == value.match(/^(?:[1-9]\d*|0)$/);
        },"Please enter  valid pincode.");

        var IsImageRequired = "true";
        $("#form_action_society").validate({
        rules: {
            society_name: {
            required: true,
            maxlength:100
        },
            society_address: {
            required: true,
            maxlength:100
        },
            society_landmark: {
            required: true,
            maxlength:50
        },
            society_city: {
            required: true,
        },
            society_state: {
            required: true,
        },
            society_pincode: {
            required: true,
            minlength:6,
            maxlength:6,
            number:true
        },
            created_by: {
            required: true,
            maxlength:100
        },
            society_post: {
            required: true,
            maxlength:50
        },                   

    },
    messages: {
        society_name: {
        required: "Society name is required!",
        maxlength:"Society name is not more than 100 character!"
    },
        society_address: {
        required: "Society address is required!",
        maxlength:"Society address is not more than 100 character!"
    },
        society_landmark: {
        required: "Society landmark is required!",
        maxlength:"Society landmark is not more than 50 character!"
    },
        society_city: {
        required: "Society city is required!"
    },
        society_state: {
        required: "Society state is required!"
    },
        society_pincode: {
        required: "Society pincode is required!",
        minlength:"Please enter valid pincode!",
        maxlength:"Pincode is not more than 6 digit!"
    },
        created_by: {
        required: "Created by name is required!",
        maxlength:"Created by is not more than 100 character!"
    },
        society_post: {
        required: "Society post required!",
        maxlength:"Society post is not more than 100 character!"
    },

    },
    errorPlacement: function (error, element) {
        var my = "";
        var at = "";

        if ($(window).width() > 800) {
        my = 'bottom right';
        at = 'top right';
        }
        else {
        my = 'top right';
        at = 'bottom right';
        }
        if (!error.is(':empty')) {
        $(element).not('.valid').qtip({
            overwrite: false,
            content: error,
            hide: false,
            show: {
                event: false,
                ready: true
            },
            position: {
                my: my,
                at: at,
                viewport: $(window),
                adjust: { x: 7, y: -2 }
            },
            style: {
                classes: 'qtip-red qtip-rounded qtip-shadow',
                tip: {
                    height: 6,
                    width: 11
                }
            }
        })
        .qtip('option', 'content.text', error);
        }
        else 
        {
            element.qtip('destroy');
        }
        },
        success: "valid",


        });

        var _URL = window.URL || window.webkitURL;

        $("#society_image").on('change', function(e) {
            var image, file;
            if ((file = this.files[0])) 
            {
                image = new Image();
                var input = $('#society_image');
                image.onload = function() 
                {
                    var height = this.height;
                    var width = this.width;
                    var ratio = width/height;
                    var size = file.size;
                    if (width != 800 || height != 600) 
                    {   
                        swal("Image size should be 800px*600px");
                        input.replaceWith(input.val('').clone(true));
                        return false;
                    }
                };
                image.src = _URL.createObjectURL(file);
            }  
        });
    });


    $("#society_state").change(function() {
        var state_id = this.value;
        $.ajax({
            url: '<?php echo site_url()."admin/society/getCityList/"; ?>',
            type: 'POST',
            data: {state_id : state_id},
            success: function(data) 
            {
                var jsonobject = JSON.parse(data);
                document.getElementById("society_city").innerHTML = "";
                $("#society_city").append(jsonobject);
            }
        });
    });

    $('#society_post_flag').on('change',function()
    {
        var value = this.value;
        if(value == 0)
        {
         $('#xxx').hide();
        }
        else
        {
            $('#xxx').show();   
        }
    });
</script>
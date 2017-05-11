<?php
defined('BASEPATH') OR exit('No direct script access allowed');

?>
<style>
    .span_error{color: #FF0000;}

</style>

            <div class="content-wrapper">
                <section class="content-header">
                    <?php echo $pagetitle; ?>
                    <?php echo $breadcrumb; ?>
                </section>
                <section class="content">
                    <div class="row">
                        <div class="col-md-12">
                             <div class="box box-success">
                                <div class="box-header with-border">
                                    <h3 class="box-title"></h3>
                                </div>
                                <div class="box-body">
                                    <div class="col-md-12">

                                    <?php echo form_open(uri_string(), array('class' => 'form-horizontal', 'id' => 'form-edit_user')); ?>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_firstname', 'first_name', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>
                                            <div class="col-sm-10">
                                                <?php echo form_input($first_name);?>
                                                 <span class="span_error"><?php echo form_error('first_name'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_lastname', 'last_name', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>
                                            <div class="col-sm-10">
                                                <?php echo form_input($last_name);?>
                                                 <span class="span_error"><?php echo form_error('last_name'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_company', 'company', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>
                                            <div class="col-sm-10">
                                                <?php echo form_input($company);?>
                                                <span class="span_error"><?php echo form_error('company'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_email', 'email', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>

                                            <div class="col-sm-10">
                                                <?php echo form_input($email);?>
                                                <span class="span_error"><?php echo form_error('email'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_phone', 'phone', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>

                                            <div class="col-sm-10">
                                                <?php echo form_input($phone);?>
                                                 <span class="span_error"><?php echo form_error('phone'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_password', 'password', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>

                                            <div class="col-sm-10">
                                                <?php echo form_input($password);?>
                                                <div class="progress" style="margin:0">
                                                    <div class="pwstrength_viewport_progress"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                            <?php echo lang('users_password_confirm', 'password_confirm', array('class' => 'control-label')); ?>
                                            <span class="span_error">*</span>
                                        </div>
                                            
                                            <div class="col-sm-10">
                                                <?php echo form_input($password_confirm);?>
                                            </div>
                                        </div>

                                        <?php if ($this->ion_auth->is_admin()): ?>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                           <label class="control-label"><?php echo lang('users_member_of_groups');?></label> 
                                        </div>
                                            
                                            <div class="col-sm-10">
                                                <?php foreach ($groups as $group):?>
                                                <?php
                                                    $gID     = $group['id'];
                                                    $checked = NULL;
                                                    $item    = NULL;

                                                    foreach($currentGroups as $grp) {
                                                        if ($gID == $grp->id) {
                                                            $checked = ' checked="checked"';
                                                            break;
                                                        }
                                                    }
                                                ?>
                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" name="groups[]" value="<?php echo $group['id'];?>"<?php echo $checked; ?>>
                                                        <?php echo htmlspecialchars($group['name'], ENT_QUOTES, 'UTF-8'); ?>
                                                    </label>
                                                </div>
                                                <?php endforeach?>
                                            </div>
                                        </div>
                                        </div>
                                        <?php endif ?>                                        
                                   
                                </div>
                                <div class="box-footer">
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <?php echo form_hidden('id', $user->id);?>
                                            <?php echo form_hidden($csrf); ?>
                                            <div class="btn-group edit_user pull-right">
                                                <?php echo form_button(array('type' => 'submit', 'class' => 'btn btn-success btn-flat', 'content' => lang('actions_update'))); ?>
                                                <?php echo anchor('admin/users', lang('actions_cancel'), array('class' => 'btn btn-default btn-flat')); ?>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <?php echo form_close();?>
                                </div>
                            </div>
                         </div>
                    </div>
                </section>
            </div>
<script>
        $(document).ready(function(){

             $.validator.addMethod("alpha", function(value, element) {
            return this.optional(element) || value == value.match(/^[a-zA-Z\s]+$/);
         },"Please enter only Alphabets.");

         $.validator.addMethod("number", function(value, element) {
            return this.optional(element) || value == value.match(/^[789][0-9]{9}$/);
         },"Please enter a valid mobile number.");
            $("#form-edit_user").validate({
                rules: {
                    first_name: {
                        required: true,
                        maxlength:50,
                        alpha:true,
                    },
                    last_name: {
                        required: true,
                        alpha:true
                    },
                    company: {
                        required: true
                    },
                    email: {
                        required: true
                    },
                    phone: {
                        required: true,
                        number:true
                    },
                    password: {
                        required: true,
                        minlength:8,
                        maxlength:40
                    },
                    password_confirm:{
                        required:true,
                        equalTo:password
                    },
                    
                 },
                messages: {
                    first_name: {
                        required: "First name is required!",
                        maxlength:"First name should not be more than 50 characters!"
                    },
                    last_name: {
                        required: "Last name is required!",
                        maxlength:"Last name should not be more than 50 characters!"
                    },
                    company: {
                        required: "Company name is required!",
                        maxlength:"Company name should not be more than 100 characters!"
                    },
                     email: {
                        required: " Email id is required!"
                    },
                    phone: {
                        required: "Phone number is required!"
                    },
                    password:{
                        required: "Password is required!",
                        maxlength:"Password should not be more than 40 characters!"
                    },
                    password_confirm:{
                        required: "Confirm password is required!",
                        equalTo:"Password does not match the confirm password!"
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
                    else {
                        element.qtip('destroy');
                    }
                },
                success: "valid",
            });

        });
    </script>
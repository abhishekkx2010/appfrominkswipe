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

                                    <?php echo form_open(current_url(), array('class' => 'form-horizontal', 'id' => 'form-create_group')); ?>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                          <?php echo lang('groups_name', 'group_name', array('class' => 'control-label')); ?>  
                                          <span class="span_error">*</span>
                                        </div>
                                            
                                            <div class="col-sm-10">
                                                <?php echo form_input($group_name);?>
                                                <span class="span_error"><?php echo form_error('group_name'); ?></span>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                        <div class="col-sm-2">
                                          <?php echo lang('groups_description', 'description', array('class' => 'control-label')); ?> 
                                          <span class="span_error">*</span> 
                                        </div>
                                            
                                            <div class="col-sm-10">
                                                <?php echo form_input($description);?>
                                                <span class="span_error"><?php echo form_error('description'); ?></span>
                                            </div>
                                        </div>                                        
                                    
                                    </div>
                                </div>
                                <div class="box-footer">
                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <div class="btn-group pull-right user_create_btn">
                                                <?php echo form_button(array('type' => 'submit', 'class' => 'btn btn-success btn-flat', 'content' => lang('actions_submit'))); ?>
                                                <?php echo anchor('admin/groups', lang('actions_cancel'), array('class' => 'btn btn-default btn-flat')); ?>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <?php echo form_close();?>
                            </div>
                         </div>
                    </div>
                </section>
            </div>
<script>
        $(document).ready(function(){

            $("#form-create_group").validate({
                rules: {
                    group_name: {
                        required: true,
                       
                    },
                    description: {
                        required: true,
                    },
                 },
                messages: {
                    group_name: {
                        required: "Group name is required!",
                    },
                    description: {
                        required: "Description is required!",
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
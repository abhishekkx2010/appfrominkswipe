<?php
defined('BASEPATH') OR exit('No direct script access allowed');

?>

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
                                    <h3 class="box-title"><?php echo sprintf(lang('users_deactivate_question'), '<span class="label label-success">'.$firstname.$lastname).'</span>';?></h3>
                                </div>
                                <div class="box-body">
                                    <div class="col-md-12">
                                    <?php echo form_open('admin/users/deactivate/'. $id, array('class' => 'form-horizontal', 'id' => 'form-status_user')); ?>
                                        <div class="form-group">
                                            <div class="col-sm-offset-2 col-sm-10">
                                                <label class="radio-inline">
                                                    <input type="radio" name="confirm" id="confirm1" value="yes" checked="checked"> <?php echo strtoupper(lang('actions_yes', 'confirm')); ?>
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio" name="confirm" id="confirm0" value="no"> <?php echo strtoupper(lang('actions_no', 'confirm')); ?>
                                                </label>
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>
                                <div class="box-footer">
                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <?php echo form_hidden($csrf); ?>
                                            <?php echo form_hidden(array('id'=>$id)); ?>
                                            <div class="btn-group pull-right user_create_btn">
                                                <?php echo form_button(array('type' => 'submit', 'class' => 'btn btn-success btn-flat', 'content' => lang('actions_submit'))); ?>
                                                <?php echo anchor('admin/users', lang('actions_cancel'), array('class' => 'btn btn-default btn-flat')); ?>
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

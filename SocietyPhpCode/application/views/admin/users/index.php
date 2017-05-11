<?php
defined('BASEPATH') OR exit('No direct script access allowed');

?>

            <div class="content-wrapper">
                <section class="content-header">
                    <?php echo $pagetitle; ?>
                    <?php echo $breadcrumb; ?>
                </section>
            <div style="margin-top:10px;"><?php echo get_alert();?></div>
                <section class="content">
                    <div class="row">
                        <div class="col-md-12">
                             <div class="box box-success">
                                <div class="box-header with-border">
                                    <h3 class="box-title"><?php echo anchor('admin/users/create', '<i class="fa fa-plus"></i> '. lang('users_create_user'), array('class' => 'btn btn-block btn-success btn-flat')); ?></h3>
                                </div>
                                <div class="box-body">
                                    <div class="col-md-12">
                                        <div class="table-responsive">
                                            <table class="table table-striped table-hover table-bordered">
                                            <thead>
                                                <tr>
                                                    <th><?php echo lang('users_firstname');?></th>
                                                    <th><?php echo lang('users_lastname');?></th>
                                                    <th><?php echo lang('users_email');?></th>
                                                    <th><?php echo lang('users_groups');?></th>
                                                    <th><?php echo lang('users_status');?></th>
                                                    <th><?php echo lang('users_action');?></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <?php foreach ($users as $user):?>
                                                <tr>
                                                    <td><?php echo htmlspecialchars($user->first_name, ENT_QUOTES, 'UTF-8'); ?></td>
                                                    <td><?php echo htmlspecialchars($user->last_name, ENT_QUOTES, 'UTF-8'); ?></td>
                                                    <td><?php echo htmlspecialchars($user->email, ENT_QUOTES, 'UTF-8'); ?></td>
                                                    <td>
                                                    <?php foreach ($user->groups as $group):?>
                                                      <!--   <?php echo anchor('admin/groups/edit/'.$group->id, '<span class="label" style="background:'.$group->bgcolor.';">'.htmlspecialchars($group->name, ENT_QUOTES, 'UTF-8').'</span>'); ?> -->

                                                      <span class="label" style="<?php echo 'background:'.$group->bgcolor; ?>"><?php echo htmlspecialchars($group->name, ENT_QUOTES, 'UTF-8'); ?></span>

                                                    <?php endforeach?>
                                                    </td>
                                                    <td><?php echo ($user->active) ? anchor('admin/users/deactivate/'.$user->id, '<span class="label label-success">'.lang('users_active').'</span>') : anchor('admin/users/activate/'. $user->id, '<span class="label label-default">'.lang('users_inactive').'</span>'); ?></td>
                                                    <td>
                                                    <a href="<?php echo base_url().'admin/users/edit/'.$user->id; ?>" class="icon edit_cat margin-10" data-id="<?php echo $user->id; ?>">
                                                       <span title="Edit" class="fa fa-pencil" aria-hidden="true"></span>
                                                     </a>&nbsp;&nbsp;
                                                     <a href="<?php echo base_url().'admin/users/profile/'.$user->id; ?>" class="icon edit_cat margin-10" data-id="<?php echo $user->id; ?>">
                                                       <span title="view" class="fa fa-eye" aria-hidden="true"></span>
                                                     </a>
                                                    </td>
                                                </tr>
                                                <?php endforeach;?>
                                            </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="box-footer">&nbsp;</div>
                            </div>
                         </div>
                    </div>
                </section>
            </div>

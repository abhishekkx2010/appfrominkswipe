<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style type="text/css">
      .table{margin-bottom: 0px !important;}
      button.btn.btn-submit.pull-right{margin-right: 10px;}
      .btn.btn-submit{border-color: #008d4c;background-color: #00a65a;color: #fff;}
        .btn.btn-submit:hover{background-color: #008d4c;border-color: #398439;}
        .btn.btn-cancle{background-color: #f39c12;border-color: #e08e0b;color:#fff;}
        .btn.btn-cancle:hover{border-color: #d58512;background-color: #e08e0b;color: #fff;}
        .th-v-top thead tr th{vertical-align: top;}
        .head-input{display: block !important; margin: 0 auto;}
        th.text-center{white-space: nowrap;}
        .mt20{margin-top: 20px;}
        .span_error{color: #FF0000;}
</style>
<div class="content-wrapper">
	<section class="content-header">
		<?php echo $pagetitle; ?>
		<?php echo $breadcrumb; ?>
	</section>
   <div id="showalert" class="alert alert-danger alert-msg" style="margin-top:10px; display:none;">Please select at least one user.</div>  
 	<div style="margin-top:10px;"><?php echo get_alert();?></div>
    <!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">         
					<div class="box box-success">
						<div class="box-header with-border">
							<h3 class="box-title"></h3>
						</div>
					<!-- /.box-header -->
						<div class="box-body">
							<?php echo form_open_multipart("admin/society/sendNotification", array('class' => 'form-horizontal', 'id' => 'form_action_notification')); ?>
							<div class="col-md-12">
								<div id="example1_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
									<div class="row">
										<div class="col-sm-12">
										<!-- <div class="table-responsive"> -->
										<?php if(!empty($Notifications)){ ?> 
											<table id="notification_user" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top" width="100%" cellspacing="0">
												<thead>
													<tr width="" id="notifi">
														<th class="text-center"><?php echo lang('Notification_appmember') ;?></th>
														<th class="text-center"><?php echo lang('app_name');?></th>
														<th class="text-center"><?php echo lang('app_member_email');?><br></th>
														<th class="text-center"><?php echo lang('app_member_City');?></th>                               
														<th class="text-center"><?php echo lang('app_member_deviceid');?></th>
														<th class="text-center"><?php echo lang('app_member_createddate');?></th>
														<th class="text-center"><?php echo lang('app_member_status');?></th>
													</tr>
												</thead>
												<tbody> 
													<?php foreach ($Notifications as $Notification){?>
														<tr class="text-center" id="notifi">
															<td><input type="checkbox" class="check" id="check_user" name="user_id[]" data-id="individual" value="<?php echo $Notification['id']; ?>" /></td>
															<td><?php echo $Notification['name']; ?></td>
															<td><?php echo $Notification['email']; ?></td>
															<td><?php echo $Notification['city']; ?></td>
															<td><?php echo $Notification['device_id']; ?></td>
															<td><?php echo $Notification['date_created']; ?></td>
															<td><?php if($Notification['status'] == "-1")
															    echo "<span class='danger'>Cancelled</span>";
															    else if($Notification['status'] == 0 || $Notification['status'] == "")
															        echo "<span class='danger'>Inactive</span>";
															    else
															        echo "<span class='status'>Active</span>";?>
															</td>
														</tr>
													<?php } ?>
												</tbody>
											</table>
										<!--</div>--->
										<?php }else { ?>
										<div class="alert alert-info">No Members found.</div>
										<?php  } ?>    
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-8">
                  <div class="row mt20">
                    <div class="col-md-3">
                      <?php echo lang('offers_label', 'offers_label', array('class' => 'control-label-coupon')); ?>
                      <span class="span_error">*</span>
                    </div>
  										<div class="col-md-9">
  												<input type="radio" name="notification" value="selected_user"  id="select_user" class="applicableto" checked="checked"/> Select User
  												&nbsp;&nbsp; <input type="radio" name="notification" value="all_user" id="select_all_user" class="applicableto"  /> All Users
  												<br>
  												<span class="span_error"><?php echo form_error('notification'); ?></span>
  										</div>
                    </div>
                    <div class="row mt20">
                        <div class="col-md-3">
                          <?php echo lang('Notification_title', 'Notification_title', array('class' => 'control-label-coupon')); ?>
                          <span class="span_error">*</span>
                        </div>
                        <div class="col-md-9">
                          <input type="text" class="form-control modal_text_width" name="notification_title" id="Notification_title" value="" placeholder="Enter Notification" maxlength="100" /><br>
                          <span class="span_error"><?php echo form_error('notification_title'); ?></span>
                      </div>
                    </div>
									
										<div class="row mt20">
                        <div class="col-md-3">
                          <?php echo lang('Notification_image', 'Notification_image', array('class' => 'control-label-coupon')); ?>
                          <span class="span_error">*</span>
                        </div>
                        <div class="col-md-9">
                          <input type="file" name="image" id="notify_image" accept=".jpg,.jpeg,.png,.bmp">
                          <span>( Upload image of Size 800px * 600px )</span>
                          <br>
                          <span class="span_error"><?php echo form_error('image'); ?></span>
                        </div>          
                    </div>
									<div class="row mt20">
                      <div class="col-md-3">
                        <?php echo lang('Notification_description', 'Notification_description', array('class' => ' control-label-coupon')); ?>
                        <span class="span_error">*</span>
                      </div>
                      <div class="col-md-9">
                        <input type="text" class="form-control modal_text_width" name="description" id="Notification_description" value="" placeholder="Enter Description" maxlength="200" /><br>
                        <span class="span_error"><?php echo form_error('description'); ?></span>
                    </div>         
                  </div>
                    <div class="row mt20">
                      <div class="col-md-3">
                        <?php echo lang('Notification_url', 'Notification_url', array('class' => 'control-label-coupon')); ?>
                      </div>
                      <div class="col-md-9">
                        <input type="url" class="form-control modal_text_width" name="url" id="Notification_url" value="" placeholder="Eg. https://www.google.com" /><br>
                        <span class="span_error"><?php echo form_error('url'); ?></span>
                      </div>
                    </div>		
								</div>
							</div>
						</div>
						<div class="box-footer">
							<button type="reset" class="btn btn-cancle pull-right">Reset</button>
							<button type="submit" class="btn btn-submit pull-right" id="submit">Submit</button>
						</div>
					</div>
				<!-- /.box-body -->
				</div>
			<?php echo form_close(); ?>
			</div>
		</section>
</div>
<script type="text/javascript">

  $(document).ready(function(){

    $('.check').click(function(){
      var cells = table.cells( ).nodes();

      var selectedIds = [];

      $(cells).find(':checked').each(function() {
          selectedIds.push($(this).val());
      });

     $('#selectedusers').val(selectedIds);
    });

    $('.applicableto').click(function(){
      var applicableto = $(this).val();
      var cells = table.cells( ).nodes();
      var allVals = [];
      if(applicableto == 'all_user')
      {
         $(cells).find(':checkbox').prop('checked', $(this).is(':checked'));
      }
      else
      {
        $(cells).find(':checkbox').prop('checked', '');
      }
    });

    $('#notification_user thead tr#notifi th').each( function () {
        var title = $('#notification_user thead th').eq( $(this).index() ).text();
        var select_array = new Array("Select User", "Status");

        if(select_array.indexOf(title) == "-1") {  
            $(this).html(title +'<input type="text" class="form-control input-sm head-input"/>' );
        }
        else{
            $(this).html(title);
        }
    });

    $("#notification_user thead input").on( 'keyup change', function () {
        table
            .column( $(this).parent().index()+':visible' )
            .search( this.value )
            .draw();
    });

    var table = $('#notification_user').DataTable( {
         orderCellsTop: true,
        "scrollX": true,
        "paging": true,
        "lengthChange": true,
        "searching": true,
        "ordering": false,
        "info": true,
        "autoWidth": true
    });

    $("#notification_user_paginate").addClass("pull-right");
    $("#notification_user_filter").remove();

    $.validator.addMethod("weburl", function(value, element) {
      return this.optional(element) || value == value.match(/\b(?:(?:https?|ftp):\/\/|www\.)[-a-z0-9+&@#\/%?=~_|!:,.;]*[-a-z0-9+&@#\/%=~_|]/i);
        },"Please enter valid url.");
    $("#form_action_notification").validate({
                rules: {
                    notification_title: {
                        required: true, 
                        maxlength:100 
                    },
                    description: {
                        required: true,
                        maxlength:200
                    },
                    notification: {
                      required: true
                    },
                    image:{
                      required:true
                    },
                    url:{
                      weburl:true
                    },
                 },
                messages: {
                    notification_title: {
                        required: "Notification title is required!",
                        maxlength:"Notification title is not more than than 100 characters!"
                    },
                    description: {
                        required: "Notification description is required!",
                        maxlength:"Notification description is not more than 200 characters!"
                    },
                    notification: {
                      required: 'Applicable to is required!'
                    },
                    image:{
                      required: 'Image is required!'
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
                submitHandler: function (form) {
                  var cells = table.cells( ).nodes();
                  var selectedIds = [];
                  $(cells).find(':checked').each(function() {
                      selectedIds.push($(this).val());
                  });
                  var checked = $('#check_user:checkbox:checked').length;
                        if(selectedIds == '')
                        {
                          $('#showalert').show();
                          window.scrollTo(500,0);
                          return false;
                        }
                        else{
                          form.submit();
                          $('#showalert').hide();
                        }
                },
                success: "valid",
            });

    var _URL = window.URL || window.webkitURL;

        $("#notify_image").on('change', function(e) {
            var image, file;
            if ((file = this.files[0])) 
            {
                image = new Image();
                var input = $('#notify_image');
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
</script>

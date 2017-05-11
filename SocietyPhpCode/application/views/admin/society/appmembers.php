<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style>
    .wdt100{width:100px;}
    .table{margin-bottom: 0px !important;}
    .th-v-top thead tr th{vertical-align: top;}
    .head-input{display: block !important; margin: 0 auto;}
    th.text-center{white-space: nowrap;}
</style>
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <?php echo $pagetitle; ?>
    <?php echo $breadcrumb; ?>
  </section>
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
            <div class="col-md-12">
             <div id="example1_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
              <div class="row">
                <div class="col-sm-6">                          
                </div>
                <div class="col-sm-6">                          
                </div>        
              </div>
              <div class="row">
               <div class="col-sm-12">
                 <!-- <div class="table-responsive"> -->
                 <?php if(!empty($allAppMembers)){ ?> 
                  <table id="app_member" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top" width="100%" cellspacing="0">
                    <thead>
                      <tr width="" id="searchbox">
                        <th class="text-center serach_box"><?php echo lang('common_sr_no');?></th>
                        <th class="text-center"><?php echo lang('app_name');?><br></th>
                        <th class="text-center"><?php echo lang('app_member_email');?><br></th>
                        <th class="text-center"><?php echo lang('app_member_City');?></th>                               
                        <th class="text-center"><?php echo lang('app_member_deviceid');?></th>
                        <th class="text-center"><?php echo lang('app_member_createddate');?></th>
                        <th class="text-center"><?php echo lang('app_member_status');?></th>
                        <th class="text-center serach_box"><?php echo lang('common_action');?></th>
                      </tr>
                    </thead>
                    <tbody>                                                 
                     <?php $count = 1;
                     foreach ($allAppMembers as $member){?>
                      <tr class="text-center" id="searchbox">
                        <td><?php echo $count++; ?></td>
                        <td><?php echo $member['name']; ?></td>
                        <td><?php echo $member['email']; ?></td>
                        <td><?php echo $member['city']; ?></td>
                        <td><?php echo $member['device_id']; ?></td>
                        <td><?php echo $member['date_created']; ?></td>
                        <td><?php if($member['status'] == "-1")
                          echo "<span class='danger'>Cancelled</span>";
                          else if($member['status'] == 0 || $member['status'] == "")
                            echo "<span class='danger'>Inactive</span>";

                          else
                            echo "<span class='status'>Active</span>";
                          ?></td>
                          <td>
                            <div class="wdt100">
                             <a href="<?php echo base_url().'admin/society/viewMember/'.$member['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $member['name']; ?>" data-id="<?php echo $member['id']; ?>">
                               <span title="View" class="fa fa-eye" aria-hidden="true"></span>
                             </a>
                             &nbsp;
                             <a href="javascript:;" class="icon user_status margin-10" value="<?php echo $member['id']; ?>" data-id="<?php echo $member['status']; ?>">
                              <?php if($member['status'] == 1){ ?>
                                <span title="Make Inactive" class="fa fa-times" aria-hidden="true"></span>
                                <?php } else { 
                                  ?>
                                  <span title="Make Active" class="fa fa-check" aria-hidden="true"></span>
                                  <?php }?>
                                </a>
                              </div>
                            </td>
                          </tr>
                          <?php };
                          ?>
                        </tbody>
                      </table>
                         <!--</div>--->
                         <?php 
                       }else { ?>
                        <div class="alert alert-info">No Member found.</div>
                        <?php 
                      } ?>
                    </div>
                  </div>
                  <div class="row">
                   <div class="col-sm-5"></div>
                 </div>
               </div>
             </div>
           </div>
         </div>
         <!-- /.box-body -->
       </div>
     </div>
   </div>
 </section>
</div>
<script type="text/javascript">

  $(document).ready(function(){
    $('#app_member thead tr#searchbox th').each( function () {
      var title = $('#app_member thead th').eq( $(this).index() ).text();
      var select_array = new Array("Sr. No.", "Action");

      if(select_array.indexOf(title) == "-1") {  
        $(this).html(title +'<input type="text" class="form-control input-sm head-input"/>' );
      }
      else{
        $(this).html(title);
      }
    });

    $("#app_member thead input").on( 'keyup change', function () {
      table
      .column( $(this).parent().index()+':visible' )
      .search( this.value )
      .draw();
    });

    var table = $('#app_member').DataTable( {
     orderCellsTop: true,
     "scrollX": true,
     "paging": true,
     "lengthChange": true,
     "searching": true,
     "ordering": false,
     "info": true,
     "autoWidth": true
   });

    $("#app_member_paginate").addClass("pull-right");
    $("#app_member_filter").remove();
  });

  $(".user_status").click(function(){

    var user_id = $(this).attr('value');
    var status = $(this).attr('data-id'); 
    $.ajax({
      type     :"POST",
      url      :'<?php echo site_url()."admin/society/updateStatus/"; ?>',
      data     : {user_id: user_id,
        status : status
      },
      success:function(data)
      { 
       window.location.reload();
     },
   });  
  });
</script>
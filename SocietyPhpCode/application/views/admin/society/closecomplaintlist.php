<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style>
    .table{margin-bottom: 0px !important;}
    .th-v-top thead tr th{vertical-align: top;}
    .head-input{display: block !important; margin: 0 auto;}
    th.text-center{white-space: nowrap;}
</style>
<div class="content-wrapper">
  <section class="content-header">
    <?php echo $pagetitle; ?>
    <?php echo $breadcrumb; ?>
  </section>
  <div class="margin-top:10px;"><?php echo get_alert();?></div>
  <section class="content">
    <div class="row">
      <div class="col-xs-12">
        <div class="box box-success">
          <div class="box-header with-border">
            <h3 class="box-title"></h3>
          </div>
          <div class="box-body">
            <div class="col-md-12">
              <div id="example1_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
                <div class="row">
                  <div class="col-sm-12">
                  <?php if(!empty($closeComplaint)) { ?>
                    <table id="close_complaint" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top">
                      <thead>
                        <tr width="" id="close_com">
                          <th class="text-center"><?php echo lang('common_sr_no'); ?></th>
                          <th class="text-center"><?php echo lang('complaint_subject');?></th>
                          <th class="text-center"><?php echo lang('complaint_status');?></th>
                          <th class="text-center"><?php echo lang('complaint_createdon');?></th>
                          <th class="text-center"><?php echo lang('common_action');?></th>
                        </tr>
                      </thead>
                      <tbody>
                      <?php $count == 1; foreach ($closeComplaint as $complaint) {?>
                        <tr class="text-center" id="close_comment">
                          <td><?php echo $count++; ?></td>
                          <td><?php echo $complaint['subject']?></td>
                          <td><?php if($complaint['status'] == "-1")
                            echo "<span class='danger'>Cancelled</span>";
                            else if ($complaint['status'] == 0 || $complaint['status'] == '')
                            echo "<span class='danger'>Inactive</span>";
                            else echo "<span class='status'>Active</span>" 
                            ?>
                          </td>
                          <td><?php echo $complaint['created_on']?></td>
                          <td><a href="<?php echo base_url().'admin/society/viewCloseComplaint/'.$complaint['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $complaint['subject']; ?>" data-id="<?php $complaint['id']; ?>">
                          <span title="View" class="fa fa-eye" aria-hidden="true"></span>
                          </td>
                        </tr>
                        <?php } ?>
                      </tbody>
                    </table>
                    <?php } else { ?>
                      <div class ="alert alert-info">No Closed Complaints found.</div>
                      <?php } ?>
                  </div>
                </div>
                <div class="row">
                  <div class="col-sm-5"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</div>
<script>
 $(document).ready(function(){
    $('#close_complaint thead tr#close_com th').each( function () {
      var title = $('#close_complaint thead th').eq( $(this).index() ).text();
      var select_array = new Array("Sr. No.", "Action","Status");

      if(select_array.indexOf(title) == "-1") {  
        $(this).html(title +'<br><input type="text" class="form-control input-sm head-input"/>' );
      }
      else{
        $(this).html(title);
      }
    });

    $("#close_complaint thead input").on( 'keyup change', function () {
      table
      .column( $(this).parent().index()+':visible' )
      .search( this.value )
      .draw();
    });

    var table = $('#close_complaint').DataTable( {
     orderCellsTop: true,
     "scrollX": true,
     "paging": true,
     "lengthChange": true,
     "searching": true,
     "ordering": false,
     "info": true,
     "autoWidth": true
   });

    $("#close_complaint_paginate").addClass("pull-right");
    $("#close_complaint_filter").remove();
  });

</script>
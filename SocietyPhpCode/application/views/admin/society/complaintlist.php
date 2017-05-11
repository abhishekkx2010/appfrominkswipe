<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style type="text/css">
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
               <div class="col-sm-12">
                 <!-- <div class="table-responsive"> -->
                 <?php if(!empty($complaint)){ ?> 
                  <table id="complaint_list" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top">
                    <thead>
                      <tr width="" id="searchrow">
                        <th class="text-center"><?php echo lang('common_sr_no');?></th>
                        <th class="text-center"><?php echo lang('complaint_subject');?></th>                               
                        <th class="text-center"><?php echo lang('complaint_status');?></th>
                        <th class="text-center"><?php echo lang('complaint_createdon');?></th>
                        <th class="text-center"><?php echo lang('common_action');?></th>
                      </tr>
                    </thead>
                    <tbody>                                                 
                     <?php $count = 1;
                     foreach ($complaint as $complaints){?>
                      <tr class="text-center" id="searchrow">
                        <td><?php echo $count++; ?></td>
                        <td><?php echo $complaints['subject']; ?></td>
                        <td><?php if($complaints['status'] == "-1")
                          echo "<span class='danger'>Cancelled</span>";
                          else if($complaints['status'] == 0 || $complaints['status'] == "")
                            echo "<span class='danger'>Inactive</span>";

                          else
                            echo "<span class='status'>Active</span>";
                          ?></td>
                          <td><?php echo $complaints['created_on']; ?></td>
                          <td>
                            <a href="<?php echo base_url().'admin/society/viewComplaint/'.$complaints['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $complaints['subject']; ?>" data-id="<?php echo $complaints['id']; ?>">
                             <span title="View" class="fa fa-eye" aria-hidden="true"></span>
                           </td>
                         </tr>
                         <?php };
                         ?>
                       </tbody>
                     </table>
                         <!--</div>--->
                         <?php 
                       }else { ?>
                        <div class="alert alert-info">No complaints found.</div>
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
    $('#complaint_list thead tr#searchrow th').each( function () {
      var title = $('#complaint_list thead th').eq( $(this).index() ).text();
      var select_array = new Array("Sr. No.", "Action","Status");

      if(select_array.indexOf(title) == "-1") {  
        $(this).html(title +'<br><input type="text" class="form-control input-sm"/>' );
      }
      else{
        $(this).html(title);
      }
    });

    $("#complaint_list thead input").on( 'keyup change', function () {
      table
      .column( $(this).parent().index()+':visible' )
      .search( this.value )
      .draw();
    });

    var table = $('#complaint_list').DataTable( {
     orderCellsTop: true,
     "scrollX": true,
     "paging": true,
     "lengthChange": true,
     "searching": true,
     "ordering": false,
     "info": true,
     "autoWidth": true
   });

    $("#complaint_list_paginate").addClass("pull-right");
    $("#complaint_list_filter").remove();
  });
</script>
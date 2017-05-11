<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <?php echo $pagetitle; ?>
    <?php echo $breadcrumb; ?>
  </section>
  <div style="margin-top:10px;"><?php echo get_alert();?></div>
  <style type="text/css">
    .wd100{width:100px;}
    .table{margin-bottom: 0px !important;}
    .th-v-top thead tr th{vertical-align: top;}
    .head-input{display: block !important; margin: 0 auto;}
  </style>
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
                 <?php if(!empty($offers)){ ?> 
                  <table id="offer_search" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top" width="100%" cellspacing="0">
                    <thead>
                      <tr width="" id="filterrow">
                        <th class="text-center actionfilter"><?php echo lang('common_sr_no');?></th>
                        <th class="text-center"><?php echo lang('offers_title');?></th>
                        <th class="text-center"><?php echo lang('created_by');?></th>   
                        <th class="text-center"><?php echo lang('offers_status');?></th>
                        <th class="text-center"><?php echo lang('offers_start_date');?></th>
                        <th class="text-center actionfilter"><?php echo lang('common_action');?></th>
                      </tr>
                    </thead>
                    <tbody>                                                 
                     <?php $count = 1;
                     foreach ($offers as $offer){ ?>
                      <tr class="text-center" id="filterrow">
                        <td><?php echo $count++; ?></td>
                        <td><?php echo $offer['title']; ?></td>
                        <td><?php echo $usersData['username']; ?></td>

                        <td><?php if($offer['status'] == "-1")
                          echo "<span class='danger'>Cancelled</span>";
                          else if($offer['status'] == 0 || $offer['status'] == "")
                            echo "<span class='danger'>Inactive</span>";

                          else
                            echo "<span class='status'>Active</span>";
                          ?></td>
                          <td><?php echo $offer['start_date']; ?></td>
                          <td>
                            <div class="wd100">
                              <a href="<?php echo base_url().'admin/society/editOffers/'.$offer['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $offer['title']; ?>" data-id="<?php echo $offer['id']; ?>">
                               <span title="Edit" class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                             </a>
                             &nbsp;
                             <a href="javascript:;" class="icon delete_offer margin-10" value="<?php echo $offer['id']; ?>">
                              <span title="Delete" class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            </a>
                            &nbsp;
                            <a href="<?php echo base_url().'admin/society/viewOffersDetails/'.$offer['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $offer['title']; ?>" data-id="<?php echo $offer['id']; ?>">
                             <span title="View" class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                           </a>
                         </div>
                       </td>
                     </tr>
                     <?php }
                     ?>
                   </tbody>
                 </table>
                         <!--</div>--->                         
                         <?php } else { ?>
                          <div class="alert alert-info">No offers found.</div>
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
             <!-- /.box-body -->
           </div>
         </div>
       </div>
     </section>
</div>

<script type="text/javascript">

    $(document).ready(function(){
      $('#offer_search thead tr#filterrow th').each( function () {
        var title = $('#offer_search thead th').eq( $(this).index() ).text();
        var select_array = new Array("Sr. No.", "Action");

        if(select_array.indexOf(title) == "-1") {  
          $(this).html(title +'<br><input type="text" class="form-control input-sm head-input"/>' );
        }
        else{
          $(this).html(title);
        }
      });

      $("#offer_search thead input").on( 'keyup change', function () {
        table
        .column( $(this).parent().index()+ ':visible' )
        .search( this.value )
        .draw();
      });

      var table = $('#offer_search').DataTable( {
       orderCellsTop: true,
       "scrollX": true,
       "paging": true,
       "lengthChange": true,
       "searching": true,
       "ordering": false,
       "info": true,
       "autoWidth": true
     });

      $("#offer_search_paginate").addClass("pull-right");
      $("#offer_search_filter").remove();
    });

    $(".delete_offer").click(function(){
      var offer_id = $(this).attr('value');

      swal({   
        title: "Are you sure?",   
        text: "",   
        type: "warning",   
        showCancelButton: true,   
        confirmButtonColor: "#DD6B55",   
        confirmButtonText: "Delete",   
        closeOnConfirm: false 
      }, function(){   
        $.ajax({
          type     :"POST",
          url      :'<?php echo site_url()."admin/society/delete_offer/"; ?>',
          data     : {offer_id: offer_id},
          success:function(data)
          { 
            swal("Deleted!", "", "success"); 
            window.location.reload();
          },
        });
      });
    }); 
</script>
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
    <div class="content-header">
        <?php echo $pagetitle; ?>
        <?php echo $breadcrumb; ?>
        <div class="clearfix"></div>
    </div>
    <div style="margin-top:10px;"><?php echo get_alert();?></div>
    <div class="content">
        <div class="row">
            <div class="col-md-12">
                <div class="box box-success">
                    <div class="box-header">&nbsp;</div>
                    <div class="box-body">
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
                             <?php if(!empty($societies)) { ?>
                                <table id="society_search" class="table table-responsive table-bordered table-striped dataTable data_table_search th-v-top">
                                    <thead>
                                        <tr width="" id="filterrow">
                                           <th class="text-center"><?php echo lang('common_sr_no');?></th>
                                           <th class="text-center"><?php echo lang('society_name');?></th>
                                           <th class="text-center"><?php echo lang('society_address');?></th>
                                           <th class="text-center">City</th>
                                           <th class="text-center">Added By</th>
                                           <th class="text-center">Post Name</th>
                                           <th class="text-center action"><?php echo lang('common_action');?></th>
                                       </tr>
                                   </thead>
                                   <tbody>
                                    <?php $count = 1;
                                    foreach ($societies as $society) { ?>
                                        <tr class="text-center">
                                            <td><?php echo $count++; ?></td>
                                            <td><?php echo $society['name']; ?></td>
                                            <td><?php echo $society['address']; ?></td>
                                            <td><?php echo $society['city']; ?></td>
                                            <td><?php if(isset($society['created_by']))echo $society['created_by']; ?></td>
                                            
                                            <td><?php echo $society['post_name']; ?></td>
                                            <td>
                                             <a href="<?php echo base_url().'admin/society/view/'.$society['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $society['name']; ?>" data-id="<?php echo $society['id']; ?>">
                                                 <span title="View" class="fa fa-eye" aria-hidden="true"></span>
                                             </a>
                                             <span>&nbsp;&nbsp;</span>
                                         </td>   
                                     </tr>
                                     <?php } ?>   
                                 </tbody>
                             </table>
                             <div class="clearfix"></div>
                             <?php echo $this->pagination->create_links();?>
                             <?php } else { ?>
                                <div class="alert alert-info">No Society Request found.</div>
                                <?php } ?>
                                <!-- </div> -->
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-footer">&nbsp;</div>
            </div>
        </div>
    </div>
</div>
</div>

<script type="text/javascript">

    $(document).ready(function(){
        $('#society_search thead tr#filterrow th').each( function () {
            var title = $('#society_search thead th').eq( $(this).index() ).text();
            var select_array = new Array("Sr. No.", "Action");

            if(select_array.indexOf(title) == "-1") {  
                $(this).html(title +'<input type="text" class="form-control input-sm head-input"/>' );
            }
            else{
                $(this).html(title);
            }
        });

        $("#society_search thead input").on( 'keyup change', function () {
            table
            .column( $(this).parent().index()+':visible' )
            .search( this.value )
            .draw();
        });

        var table = $('#society_search').DataTable( {
            orderCellsTop: true,
            "scrollX": true,
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "ordering": false,
            "info": true,
            "autoWidth": true
        });

        $("#society_search_paginate").addClass("pull-right");
        $("#society_search_filter").remove();
    });

</script>
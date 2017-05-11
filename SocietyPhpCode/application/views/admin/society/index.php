<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style type="text/css">
    .action input{display: none !important;}
    .wdt100{width:100px;}
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
                    <div class="box-header"><h3 class="box-title"></h3></div>
                    <div class="box-body">
                        <div class="col-md-12">
                            <?php if(!empty($societies)){ ?>
                                <table id="society_search" class="table table-striped table-hover table-bordered th-v-top">
                                    <thead>
                                        <tr id="filterrow">
                                            <th class="text-center action"><?php echo lang('common_sr_no');?></th>
                                            <th class="text-center"><?php echo lang('society_name');?></th>
                                            <th class="text-center"><?php echo lang('society_address');?></th>
                                            <th class="text-center">City</th>
                                            <th class="text-center">Status</th>
                                            <th class="text-center">Added By</th>
                                            <th class="text-center action">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <?php $count = 1;
                                        foreach ($societies as $society){?>
                                            <tr class="text-center">
                                                <td><?php echo $count++; ?></td>
                                                <td><?php echo $society['name']; ?></td>
                                                <td><?php echo $society['address']; ?></td>
                                                <td><?php echo $society['city']; ?></td>
                                                <td><?php if($society['status'] == "-1")
                                                    echo "<span class='danger'>Cancelled</span>";
                                                    else if($society['status'] == 0 || $society['status'] == "")
                                                        echo "<span class='danger'>Inactive</span>";
                                                    else
                                                        echo "<span class='status'>Active</span>";
                                                    ?></td>
                                                    <td><?php if(isset($society['created_by']))echo $society['created_by']; ?></td>
                                                    <td>
                                                        <div class="wdt100">
                                                           <span>&nbsp;&nbsp;</span>
                                                           <a href="<?php echo base_url().'admin/society/edit/'.$society['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $society['name']; ?>" data-id="<?php echo $society['id']; ?>">
                                                             <span title="Edit" class="fa fa-pencil" aria-hidden="true"></span>
                                                         </a>
                                                         <span>&nbsp;&nbsp;</span>
                                                         <a href="javascript:;" class="icon delete_society margin-10" value="<?php echo $society['id']; ?>">
                                                            <span title="Delete" class="fa fa-trash" aria-hidden="true"></span>
                                                        </a>
                                                        <span>&nbsp;&nbsp;</span>
                                                        <a href="<?php echo base_url().'admin/society/viewSociety/'.$society['id']; ?>" class="icon edit_cat margin-10" value="<?php echo $society['name']; ?>" data-id="<?php echo $society['id']; ?>">
                                                           <span title="View" class="fa fa-eye" aria-hidden="true"></span>
                                                       </a>                                           
                                                   </div>   
                                               </td>
                                           </tr>
                                           <?php }; ?>    
                                       </tbody>
                                   </table>
                                   <?php } else { ?>
                                <div class="alert alert-info">No Society found.</div>
                                <?php } ?>
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
            var select_array = new Array("Society ID", "View Details");

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

    $(".delete_society").click(function(){
        var society_id = $(this).attr('value');

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
                url      :'<?php echo site_url()."admin/society/delete_society/"; ?>',
                data     : {society_id: society_id},
                success:function(data)
                { 
                    swal("Deleted!", "", "success"); 
                    window.location.reload();
                },
            });
        });
    });
</script>
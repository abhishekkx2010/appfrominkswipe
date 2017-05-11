<style type="text/css">
  .sorting{ text-align: center;}
  button.btn.btn-primary.pull-right.in{margin-right: 10px;}
</style>
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
   <?php echo $pagetitle; ?>
   <?php echo $breadcrumb; ?>
   <div class="clearfix"></div>  
 </section>
 <div style="margin-top:10px;"><?php echo get_alert();?></div>
 <section class="content">
  <div class="row">
    <div class="col-md-12">
     <div class="box box-success">
      <div class="box-header with-border">
       <h3 class="box-title">Complaint Id : <span><?php if(isset($viewComplaint[0]['id'])){echo $viewComplaint[0]['id'];} ?></span></h3>
     </div>
     <!-- .box-header -->
     <!-- form start -->
     <div class="box-body">
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group box-header">
            <dt>Created By :</dt>
            <dd><a href="<?php echo base_url().'admin/society/viewMember/'.$viewComplaint[0]['created_by'];?>"><?php if(isset($viewComplaint[0]['user_name'])){echo $viewComplaint[0]['user_name'];}?></a></dd>
          </div>
          <div class="form-group box-header">
            <dt>Society Name :</dt>
            <dd><?php if(isset($viewComplaint[0]['society_name'])){echo $viewComplaint[0]['society_name'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Subject:</dt>
            <dd><?php if(isset($viewComplaint[0]['subject'])){echo $viewComplaint[0]['subject'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Created Date :</dt>
            <dd><?php if($viewComplaint[0]['created_on']) {echo $viewComplaint[0]['created_on'];}?></dd>
          </div>
        </dl>
      </div>
      <div class="col-md-6">
       <dl class="dl-horizontal">                     
        <div class="form-group box-header">
          <dt>Processed By :</dt>
          <dd><?php echo $viewComplaint[0]['username']?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Processed On:</dt>
          <dd><?php if($viewComplaint[0]['processed_on']){echo $viewComplaint[0]['processed_on'];} else{echo "Na";}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Status :</dt>
          <dd><?php if($viewComplaint[0]['status'] == 1){echo "Open";} else{echo "Closed";}?></dd> 
        </div>
      </dl>
    </div>
      <div class="col-md-12">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Description :</dt>
          <dd><?php if(isset($viewComplaint[0]['description'])){echo $viewComplaint[0]['description'];}?></dd>
        </div>
      </dl>
    </div>
     <div class="col-md-12">
    <dl class="dl-horizontal">
      <div class="form-group box-header">
        <dt>Comment :</dt>
        <dd><?php if(isset($viewComplaint[0]['comments'])){echo $viewComplaint[0]['comments'];}?></dd>
      </div>
    </dl>
  </div>
  </div> 
  <!-- /.box-body -->  
  <!-- <div class="box-footer">&nbsp;</div>           -->
</div>
<div class="clearfix"></div>
</div>          
</div>                       
</section>
<!-- /.content -->
</div>

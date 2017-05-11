
<style>
  .btn.btn-comment{border-color:#008d4c;background-color:#00a65a;color: #fff;}
  .btn.btn-comment:hover{background-color: #008d4c;}
  .sorting{ text-align: center;} 
  .span_error{color: #FF0000;}  
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
       <h3 class="box-title">Complaint Id : <span><?php if(isset($userComplaint[0]['id'])){echo $userComplaint[0]['id'];} ?></span></h3>
     </div>
     <!-- .box-header -->
     <!-- form start -->
     <div class="box-body">
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group box-header">
            <dt>Created By :</dt>
            <dd><a href="<?php echo base_url().'admin/society/viewMember/'.$userComplaint[0]['created_by'];?>"><?php if(isset($userComplaint[0]['user_name'])){echo $userComplaint[0]['user_name'];}?></a></dd>
          </div>
          <div class="form-group box-header">
            <dt>Society Name :</dt>
            <dd><?php if(isset($userComplaint[0]['society_name'])){echo $userComplaint[0]['society_name'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Subject:</dt>
            <dd><?php if(isset($userComplaint[0]['subject'])){echo $userComplaint[0]['subject'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Created Date :</dt>
            <dd><?php if($userComplaint[0]['created_on']) {echo $userComplaint[0]['created_on'];}?></dd> 
          </div>
        </dl>
      </div>
      <div class="col-md-6">
       <dl class="dl-horizontal">                     
        <div class="form-group box-header">
          <dt>Processed By :</dt>
          <dd><?php if($userComplaint[0]['processed_by']){echo $userComplaint[0]['processed_by'];} else {echo "Na";}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Processed On:</dt>
          <dd><?php if($userComplaint[0]['processed_on']){echo $userComplaint[0]['processed_on'];} else{echo "Na";}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Status :</dt>
          <dd><?php if($userComplaint[0]['status'] == 1){echo "Open";} else{echo "Closed";}?></dd> 
        </div>
      </dl>
    </div>
      <div class="col-md-12">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Description :</dt>
          <dd><?php if(isset($userComplaint[0]['description'])){echo $userComplaint[0]['description'];}?></dd>
        </div>
      </dl>
    </div>
  </div>
  
  <!-- /.box-body -->  
 <!--  <div class="box-footer">&nbsp;</div> -->          
</div>
<div class="clearfix"></div>
<div class="box box-success">
  <div class="box-header with-border">
   <h3 class="box-title">Comment <span class="span_error">*</span> </h3>
 </div>
 <?php echo form_open(current_url(), array('id' => 'close_complaint')); ?>
 <div class="box-body">
  <div class="col-md-12">
    <div class="form-group">
      <textarea class="form-control" placeholder="Enter your comment" maxlength="500" name="comments" id="add_comments" style="resize:vertical;"></textarea>
    </div>
    <span class="span_error"><?php echo form_error('comments'); ?></span>
  </div>
</div>
<div class="box-footer">
  <div class="col-md-12">
    <button type="submit" class="btn btn-comment pull-right">Submit</button>
  </div>
</div> 
<?php echo form_close();?>  
</div> 
</div>          
</div>                     
</section>
<!-- /.content -->
</div>

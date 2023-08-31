package vttp2023.batch3.csf.assessment.cnserver.repositories;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;
	
	// TODO: Task 1
	    public String uploadFile(MultipartFile file) {
        ObjectMetadata metaData = new ObjectMetadata();
             metaData.setContentType(file.getContentType());
             metaData.setContentLength(file.getSize());
             metaData.addUserMetadata("name", file.getOriginalFilename());
             System.out.println(file.getOriginalFilename());

             try  {
            System.out.println("trying to put files");
             PutObjectRequest putReq = new PutObjectRequest("newsimage", file.getOriginalFilename(), file.getInputStream(), 
             metaData);
             putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
             try {
                 s3.putObject(putReq);
                System.out.println("Upload successful");
             } catch(Exception ex)  {
               System.out.println("nope");
                ex.printStackTrace();
             }

             } catch(IOException ex) {
                ex.printStackTrace();
             }
              
             return file.getOriginalFilename();
    }

		public String getURL(String fileName) {
		return s3.getUrl("newsimage", fileName).toExternalForm();
		}

	




}

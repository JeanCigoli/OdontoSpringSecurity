package br.senai.sp.odonto.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {
	
	@PostConstruct
	public void init() throws IOException {
		
		// Verificando se existe alguma instância do firebase
		if( FirebaseApp.getApps().isEmpty() ) {
			
			
			// Transformando o arquivo com as credenciais para bits
			InputStream dataAuthFirebase = 
					FirebaseStorageService.class.getResourceAsStream("/firebaseAccountKey.json");
			
			// Passando a autenticação para o firebase
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(dataAuthFirebase))
					.setStorageBucket("odonto-project.appspot.com")
					.setDatabaseUrl("https://odonto-project.firebaseio.com")
					.build();
			
			FirebaseApp.initializeApp(options);
			
		}
		
	}
	
	public String upload(UploadInput uploadInput) {
		
		Bucket bucket = StorageClient.getInstance().bucket();
		
		byte[] bytes = Base64.getDecoder().decode(uploadInput.getBase64());
		String filename = uploadInput.getFilename();
		String mimeType = uploadInput.getMimeType();
		
		Blob arquivo = bucket.create(filename, bytes, mimeType);
		
		arquivo.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		
		/*
		  Para concatenar a url usamos o format e indicamos que com o %s será substituido pelo argumento;
		*/
		return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), filename);
		
	}

}

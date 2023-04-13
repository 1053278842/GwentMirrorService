package com.ll.gwentmirror;

import com.ll.gwentmirror.entity.FTPBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Administrator
 */
@SpringBootApplication

public class GwentMirrorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwentMirrorApplication.class, args);
	}

}

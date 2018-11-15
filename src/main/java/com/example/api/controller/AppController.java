package com.example.api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.api.model.Greeting;
import com.example.cmd.callCMD;
import com.example.storage.callStorage;
import com.google.gson.Gson;

@RestController
public class AppController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping("/startDownload")
	public String downloadFromStorage() {
		String msg = null;
		try {
			callStorage storage = new callStorage();
			String res = storage.downLoadFromStorage();
			msg = res;
		} catch (Exception e) {
			msg = e.toString();
		}
		return msg;
	}

	@RequestMapping("/startTest")
	public String startCMDUnitTest() {
		String msg = null;
		try {
			callCMD call = new callCMD();
			String res = call.executeTestCommand();
			msg = res;
		} catch (Exception e) {
			msg = e.toString();
		}
		return msg;
	}

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		String msg = null;

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			msg = "Please select a file to upload";
			return msg;
		}

		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(System.getProperty("user.home") + "/Downloads/" + file.getOriginalFilename());
			Files.write(path, bytes);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");
			msg = "You successfully uploaded";

		} catch (IOException e) {
			msg = e.toString();
		}

		return msg;
	}

}
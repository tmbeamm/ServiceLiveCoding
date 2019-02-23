package com.example.api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.api.model.WorkspaceModelCustom;
import com.example.api.model.WorkspaceTree;
import com.example.bean.TreeViewBean;
import com.example.cmd.callCMD;
import com.example.service.WorkspaceTreeService;
import com.example.storage.callStorage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
public class AppController {

	@Autowired
	private WorkspaceTreeService wsService;

	@RequestMapping("/startDownload")
	public ResponseEntity<Map<String, String>> downloadFromStorage() {
		String msg = null;
		Map<String, String> result = new HashMap<>();
		try {
			callStorage storage = new callStorage();
			String res = storage.downLoadFromStorage();
			msg = res;
			result.put("response", msg);

		} catch (Exception e) {
			msg = e.toString();
			result.put("response", msg);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping("/startTest")
	public ResponseEntity<Map<String, String>> startCMDUnitTest() {
		String msg = null;
		Map<String, String> result = new HashMap<>();
		try {
			callCMD call = new callCMD();
			String res = call.executeTestCommand();
			msg = res;
			result.put("response", msg);
		} catch (Exception e) {
			msg = e.toString();
			result.put("response", msg);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		System.err.println(new Timestamp(System.currentTimeMillis()));
		String msg = null;

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			msg = "Please select a file to upload";
			return msg;
		}

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(System.getProperty("user.home") + "/Downloads/" + file.getOriginalFilename());
			Files.write(path, bytes);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");
			msg = "You successfully uploaded";

		} catch (IOException e) {
			msg = e.toString();
		}
		System.err.println(new Timestamp(System.currentTimeMillis()));
		return msg;
	}

	@RequestMapping("/genTree")
	public ResponseEntity<TreeViewBean> genereateTreeView(@RequestParam String name) {
		List<WorkspaceModelCustom> listWS = wsService.findWSByName(name);
		TreeViewBean result = new TreeViewBean();
		List<TreeViewBean> listBean = new ArrayList();
		Map<Integer, List<WorkspaceModelCustom>> map = new TreeMap();
		Map<String, List<TreeViewBean>> mapParent = new TreeMap();
		int maxRoot = Integer.MIN_VALUE;

		for (WorkspaceModelCustom ws : listWS) {
			if (ws.getLevel() >= maxRoot) {
				maxRoot = ws.getLevel();
			}
			if (map.get(ws.getLevel()) == null) {
				List<WorkspaceModelCustom> tmpList = new ArrayList();
				tmpList.add(ws);
				map.put(ws.getLevel(), tmpList);
			} else {
				List<WorkspaceModelCustom> tmpList = map.get(ws.getLevel());
				tmpList.add(ws);
				map.put(ws.getLevel(), tmpList);
			}
		}

		for (int i = maxRoot; i > 1; i--) {
			List<WorkspaceModelCustom> currentLevelList = map.get(i);

			for (WorkspaceModelCustom ws : currentLevelList) {
				List<TreeViewBean> listBeanTmp = new ArrayList();
				TreeViewBean tmpBean = new TreeViewBean();
				tmpBean.setName(ws.getName());
				if (mapParent.get(ws.getName()) != null) {
					tmpBean.setChildren(mapParent.get(ws.getName()));
				}
				if (mapParent.get(ws.getParent()) != null) {
					listBeanTmp = mapParent.get(ws.getParent());
					listBeanTmp.add(tmpBean);
					mapParent.put(ws.getParent(), listBeanTmp);
				} else {
					listBeanTmp.clear();
					listBeanTmp.add(tmpBean);
					mapParent.put(ws.getParent(), listBeanTmp);
				}
				listBean = listBeanTmp;
			}
		}

		result.setName(name);
		result.setChildren(listBean);
		result.setToggled(true);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping("/testList")
	@ResponseBody
	public ResponseEntity<List<WorkspaceModelCustom>> test(@RequestParam String name) {

		return new ResponseEntity<>(wsService.findWSByName(name), HttpStatus.OK);
	}

}
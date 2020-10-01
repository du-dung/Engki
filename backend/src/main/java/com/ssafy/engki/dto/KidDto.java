package com.ssafy.engki.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class KidDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class KidInfo {
		@ApiModelProperty(value = "Kid id", example = "1")
		private long id;

		@ApiModelProperty(value = "Kid name", example = "김아가")
		private String name;

		@JsonFormat(pattern = "yyyy-MM-dd")
		@ApiModelProperty(value = "Kid birthday", example = "2015-03-28")
		private Date birthday;

		@ApiModelProperty(value = "Kid icon file path", example = "/img/icon/fairytale/002-wizard.png")
		private String icon;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class KidRequest {
		@ApiModelProperty(value = "Parent id", example = "1486633352")
		private long parentId;

		@ApiModelProperty(value = "Kid name", example = "김아가")
		private String name;

		@JsonFormat(pattern = "yyyy-MM-dd")
		@ApiModelProperty(value = "Kid birthday", example = "2015-03-28")
		private Date birthday;

		@ApiModelProperty(value = "Kid icon file path", example = "/img/icon/fairytale/002-wizard.png")
		private String icon;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class KidIcon {
		@ApiModelProperty(value = "Kid icon file path", example = "/img/icon/fairytale/002-wizard.png")
		private String icon;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class KidSticker {
		@ApiModelProperty(value = "Sticker id", example = "1")
		private long id;

		@ApiModelProperty(value = "Sticker file path", example = "/img/icon/jungle/001-toucan.png")
		private String filePath;
	}
}
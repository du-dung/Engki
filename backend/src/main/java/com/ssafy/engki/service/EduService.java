package com.ssafy.engki.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.ssafy.engki.dto.EduDto;
import com.ssafy.engki.entity.Image;
import com.ssafy.engki.entity.ImageCaption;
import com.ssafy.engki.entity.ImageWord;
import com.ssafy.engki.entity.Word;
import com.ssafy.engki.mapper.EduMapper;
import com.ssafy.engki.repository.ImageCaptionRepository;
import com.ssafy.engki.repository.ImageRepository;
import com.ssafy.engki.repository.ImageWordRepository;
import com.ssafy.engki.repository.ThemeRepository;
import com.ssafy.engki.repository.WordRepository;

@RequiredArgsConstructor
@Service
public class EduService {
	private final ThemeRepository themeRepository;
	private final WordRepository wordRepository;
	private final ImageRepository imageRepository;
	private final ImageWordRepository imageWordRepository;
	private final ImageCaptionRepository imageCaptionRepository;

	public List<EduDto.Theme> getThemes() {
		return EduMapper.INSTANCE.toTheme(themeRepository.findAll());
	}

	public EduDto.Word getRandomImage(long themeId, long kidId) {
		Random rand = new Random(System.currentTimeMillis());

		// themeId의 단어 목록 중, 1. 아이가 배우지 않은 단어 중 랜덤
		List<com.ssafy.engki.entity.Word> words = wordRepository.getUnlearnedWord(themeId, kidId);
		com.ssafy.engki.entity.Word word;
		if (!words.isEmpty()) {
			word = words.get(rand.nextInt(words.size()));
		} else { // 배우지 않은 단어가 없음
			// 2. 가장 오래전에 배운 단어 고르기
			word = wordRepository.getOldestLearnedWord(themeId, kidId);
			System.out.println(word);
		}

		// 이 단어에 해당하는 사진 중, 이 단어만 있는 사진(다른 word가 없는 것) 고르기
		List<Image> images = imageRepository.getOneObjectImagesOfWord(word.getId());
		Image image = images.get(rand.nextInt(images.size()));

		return EduDto.Word.builder()
			.id(word.getId())
			.word(word.getWord())
			.filePath(image.getFilePath())
			.build();
	}

	public List<EduDto.Image> getRandomImages(long wordId) {
		Random rand = new Random(System.currentTimeMillis());

		List<EduDto.Image> images = new ArrayList<>();
		Word word = wordRepository.getOne(wordId);

		// word의 이미지 3개
		List<Image> wordImages = imageRepository.getOneObjectImagesOfWord(wordId);
		rand.ints(3, 0, wordImages.size()).forEach(idx ->
			images.add(EduDto.Image.builder()
				.word(word.getWord())
				.filePath(wordImages.get(idx).getFilePath())
				.build())
		);

		// word와 같은 theme의 이미지 중, wordId가 아닌 이미지 3개
		List<ImageWord> notWordImages = imageWordRepository.getOneObjectImagesFromThemeNotWord(word.getThemeId(),
			wordId);
		rand.ints(3, 0, notWordImages.size()).forEach(idx ->
			images.add(EduDto.Image.builder()
				.word(notWordImages.get(idx).getWord().getWord())
				.filePath(notWordImages.get(idx).getImage().getFilePath())
				.build()
			)
		);

		// -> 6개 리턴
		return images;
	}

	public EduDto.Segmentation getSegmentation(long wordId) {
		Random rand = new Random(System.currentTimeMillis());

		List<ImageWord> imageWords = imageWordRepository.getAllByWordId(wordId);
		ImageWord imageWord = imageWords.get(rand.nextInt(imageWords.size()));

		return EduDto.Segmentation.builder()
			.filePath(imageWord.getImage().getFilePath())
			.segFilePath(imageWord.getBoundary())
			.build();
	}

	public EduDto.Caption getCaption(long wordId) {
		Random rand = new Random(System.currentTimeMillis());

		List<ImageCaption> imageCaptions = imageCaptionRepository.getAllByWordId(wordId);
		ImageCaption imageCaption = imageCaptions.get(rand.nextInt(imageCaptions.size()));

		List<String> captionsExceptWord = imageCaptionRepository.findExceptWord(wordId);
		List<String> randomCaptions = new ArrayList<>();
		rand.ints(3, 0, captionsExceptWord.size()).forEach(idx ->
			randomCaptions.add(captionsExceptWord.get(idx))
		);

		return EduDto.Caption.builder()
			.filePath(imageCaption.getImage().getFilePath())
			.caption(imageCaption.getCaption())
			.randomCaptions(randomCaptions)
			.tokens(tokenize(imageCaption.getCaption()))
			.build();
	}

	private List<EduDto.Token> tokenize(String caption) {
		caption = caption.replace(".", ""); //. 없애기
		List<EduDto.Token> tokens = new ArrayList<>();

		String[] splitCaption = caption.split(" ");
		for (int i = 0; i < splitCaption.length; i++) {
			tokens.add(new EduDto.Token(splitCaption[i], i));
		}

		Collections.shuffle(tokens, new Random(System.currentTimeMillis()));

		return tokens;
	}
}
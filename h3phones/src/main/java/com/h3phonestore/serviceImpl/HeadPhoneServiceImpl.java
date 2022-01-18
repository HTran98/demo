package com.h3phonestore.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.SearchProductDetailDto;
import com.h3phonestore.entity.HeadPhoneDetail;
import com.h3phonestore.repository.HeadPhoneRepository;
import com.h3phonestore.service.HeadPhoneService;

import javassist.NotFoundException;

@Service
@Transactional
public class HeadPhoneServiceImpl implements HeadPhoneService {
	@Autowired
	HeadPhoneRepository headPhoneRepository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<HeadPhoneDetailDto> getAll() {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public void deleteHeadPhone(long headPhoneId, String updatedBy) {

		try {
			headPhoneRepository.findByHeadPhoneIdAndDeleteFlag(headPhoneId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			Date updatedDate = new Date();
			headPhoneRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, headPhoneId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
	}

	@Override
	public HeadPhoneDetailDto updateHeadPhone(HeadPhoneDetailDto heDetailDto) {
		HeadPhoneDetailDto headPhone = null;
		try {
			HeadPhoneDetail headPhoneEntity = headPhoneRepository
					.findByHeadPhoneIdAndDeleteFlag(heDetailDto.getHeadPhoneId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			Date updatedDate = new Date();

			heDetailDto.setUpdatedDate(updatedDate);
			heDetailDto.setDeleteFlag(headPhoneEntity.isDeleteFlag());
			heDetailDto.setCreatedBy(headPhoneEntity.getCreatedBy());
			heDetailDto.setCreatedDate(headPhoneEntity.getCreatedDate());
			heDetailDto.setVote(headPhoneEntity.getVote());
            int quantityImport = heDetailDto.getNewQuantityImport()+ headPhoneEntity.getQuantityImport();
            heDetailDto.setQuantityImport(quantityImport);
            
			if (StringUtils.isEmpty(heDetailDto.getImageOverView())) {
				heDetailDto.setImageOverView(headPhoneEntity.getImageOverView());
			}
			if (StringUtils.isEmpty(heDetailDto.getImageUnder())) {
				heDetailDto.setImageUnder(headPhoneEntity.getImageUnder());
			}
			if (StringUtils.isEmpty(heDetailDto.getImageSide())) {
				heDetailDto.setImageSide(headPhoneEntity.getImageSide());
			}
			if (StringUtils.isEmpty(heDetailDto.getImageOther())) {
				heDetailDto.setImageOther(headPhoneEntity.getImageOther());
			}
			headPhoneEntity = modelMapper.map(heDetailDto, HeadPhoneDetail.class);

			headPhoneEntity = headPhoneRepository.save(headPhoneEntity);

			headPhone = modelMapper.map(headPhoneEntity, HeadPhoneDetailDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return headPhone;
	}

	@Override
	public HeadPhoneDetailDto getById(long headPhoneId) {

		HeadPhoneDetailDto headPhone = null;
		try {
			HeadPhoneDetail headPhoneEntity = headPhoneRepository
					.findByHeadPhoneIdAndDeleteFlag(headPhoneId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));

			headPhone = modelMapper.map(headPhoneEntity, HeadPhoneDetailDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return headPhone;
	}

	@Override
	public List<HeadPhoneDetailDto> getByBrand(long brandId) {

		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findByProductBrandInfoBrandIdAndDeleteFlag(brandId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public List<HeadPhoneDetailDto> filterHeadPhoneDetail(SearchProductDetailDto searchDto) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		List<HeadPhoneDetail> listHeadPhoneEntity = null;
		long productId = Long.parseLong(searchDto.getProductId().split(" ")[2]);

		try {
			if (searchDto.getBrandId() == 0 && productId != 0) {
				listHeadPhoneEntity = headPhoneRepository
						.findByProductProductIdAndDeleteFlag(productId, Constants.DELETE_FLAG)
						.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			} else {
				if (searchDto.getBrandId() != 0 && productId == 0) {
					listHeadPhoneEntity = headPhoneRepository
							.findByProductBrandInfoBrandIdAndDeleteFlag(searchDto.getBrandId(), Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
				} else {
					listHeadPhoneEntity = headPhoneRepository
							.findByProductProductIdAndProductBrandInfoBrandIdAndDeleteFlag(productId,
									searchDto.getBrandId(), Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
				}
			}

			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public List<HeadPhoneDetailDto> searchHeadPhone(String keyWords) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findByProductProductNameContainingAndDeleteFlag(keyWords, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public HeadPhoneDetailDto createHeadPhone(HeadPhoneDetailDto heDetailDto) {
		Date createdDate = new Date();
		Date updatedDate = new Date();
		heDetailDto.setDeleteFlag(Constants.DELETE_FLAG);
		heDetailDto.setUpdatedDate(updatedDate);
		heDetailDto.setCreatedDate(createdDate);
		heDetailDto.setVote(5);

		HeadPhoneDetail headPhoneEntity = modelMapper.map(heDetailDto, HeadPhoneDetail.class);
		headPhoneEntity = headPhoneRepository.save(headPhoneEntity);
		return modelMapper.map(headPhoneEntity, HeadPhoneDetailDto.class);
	}

	@Override
	public List<HeadPhoneDetailDto> saveData(List<HeadPhoneDetailDto> listHeadPhoneDto) {
		List<HeadPhoneDetailDto> listHeadPhoneDetail = null;

		Date date = new Date();

		for (int i = 0; i < listHeadPhoneDto.size(); i++) {
			listHeadPhoneDto.get(i).setCreatedDate(date);
			listHeadPhoneDto.get(i).setUpdatedDate(date);
		}
		List<HeadPhoneDetail> listHeadPhoneDetailEntity = listHeadPhoneDto.stream()
				.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetail.class)).collect(Collectors.toList());

		listHeadPhoneDetailEntity = headPhoneRepository.saveAll(listHeadPhoneDetailEntity);

		listHeadPhoneDetail = listHeadPhoneDetailEntity.stream()
				.map(headPhoneEntity -> modelMapper.map(headPhoneEntity, HeadPhoneDetailDto.class))
				.collect(Collectors.toList());

		return listHeadPhoneDetail;
	}

	@Override
	public List<HeadPhoneDetailDto> getProductByPrice(int minPrices, int maxPrices) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findByDeleteFlagAndPricesBetween(Constants.DELETE_FLAG, minPrices, maxPrices)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;

	}

	@Override
	public List<HeadPhoneDetailDto> getProductByBrand(List<String> brandNameList) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findByDeleteFlagAndProductBrandInfoBrandNameIn(Constants.DELETE_FLAG, brandNameList)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public List<HeadPhoneDetailDto> getProductByVote(float minVote, float maxVote) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findByDeleteFlagAndVoteBetween(Constants.DELETE_FLAG, minVote, maxVote)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public List<HeadPhoneDetailDto> getProductOther(String brandName, int prices,long headPhoneId) {
		List<HeadPhoneDetailDto> listHeadPhone = null;

		try {
			List<HeadPhoneDetail> listHeadPhoneEntity = headPhoneRepository
					.findTop4DistinctByDeleteFlagAndPricesLessThanEqualOrProductProductNameAndHeadPhoneIdNotOrderByQuantityExportDesc(
							Constants.DELETE_FLAG, prices, brandName,headPhoneId)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));
			listHeadPhone = listHeadPhoneEntity.stream()
					.map(headPhone -> modelMapper.map(headPhone, HeadPhoneDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listHeadPhone;
	}

	@Override
	public void updateVote(float vote, long headPhoneId) {

		try {
			headPhoneRepository.findByHeadPhoneIdAndDeleteFlag(headPhoneId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có sản phẩm phù hợp"));

			headPhoneRepository.updateVote(vote, headPhoneId);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

}

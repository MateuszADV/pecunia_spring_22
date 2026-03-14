package pecunia_22.services.medalService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pecunia_22.exceptions.CountryNotFoundException;
import pecunia_22.exceptions.ResourceNotFoundException;
import pecunia_22.mapper.MedalMapper;
import pecunia_22.models.Medal;
import pecunia_22.models.dto.medal.MedalDto;
import pecunia_22.models.repositories.MedalRepository;
import pecunia_22.models.sqlClass.CountryByStatus;
import pecunia_22.models.sqlClass.CurrencyByStatus;
import pecunia_22.services.userService.CurrentUserServiceImpl;
import pecunia_22.services.validate.ValidationServiceImpl;
import pecunia_22.timing.annotation.MeasureTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MedalServiceImpl implements MedalService {

    private final MedalRepository medalRepository;
    private final CurrentUserServiceImpl currentUserService;
    private final ValidationServiceImpl validationService;
    private final MedalMapper medalMapper;

    @Autowired
    public MedalServiceImpl(MedalRepository medalRepository, CurrentUserServiceImpl currentUserService, ValidationServiceImpl validationService, MedalMapper medalMapper) {
        this.medalRepository = medalRepository;
        this.currentUserService = currentUserService;
        this.validationService = validationService;
        this.medalMapper = medalMapper;
    }

    @Override
    public List<Medal> getAllMedal() {
        return medalRepository.findAll();
    }

    @Override
    public void saveMedal(Medal medal) {
        this.medalRepository.save(medal);
    }



    @Override
    public MedalDto getMedalDtoById(Long id) {

        if (currentUserService.isAdmin()) {
            Medal medal = medalRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            return medalMapper.toDto(medal);
        } else {
            Medal medal = medalRepository.findByIdAndVisibleTrue(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            return medalMapper.toDto(medal);
        }
    }

    @Override
    public Medal getMedalById(Long id) {

        validationService.validateMedal(id);

        Optional<Medal> medal;

        if (currentUserService.isAdmin()) {
            medal = medalRepository.findById(id);
            System.out.println("***********************************************8888888888888");
            System.out.println("ADMIN");
            System.out.println("***********************************************8888888888888");

        } else {
            medal = medalRepository.findByIdAndVisibleTrue(id);
            System.out.println("***********************************************8888888888888");
            System.out.println("USER");
            System.out.println("***********************************************8888888888888");
        }

        return medal.orElseThrow(() -> new ResourceNotFoundException(id));


//        validationService.validateMedal(id);
//        Optional<Medal> result;
//
//        if (currentUserService.isAdmin()) {
//            result = medalRepository.findById(id);
//        } else {
//            result = medalRepository.findById(id);
//            if (result.isEmpty()) {
//                throw new ResourceNotFoundException(id);
//            }
//        }
//
//        return result.get();

//        if (currentUserService.isAdmin()) {
//            return medalRepository.findById(id)
//                    .orElseThrow(() -> {
//                        log.warn("Medal with id {} not found", id);
//                        return new RuntimeException("Medal not found");
//                    });
//        }
//
//        return medalRepository.findByIdAndVisibleTrue(id)
//                .orElseThrow(() -> {
//                    log.warn("User tried to access hidden or non-existing medal id {}", id);
//                    return new RuntimeException("Medal not found");
//                });


//        Optional<Medal> optional = medalRepository.findById(id);
//        if (optional.isPresent()) {
//            return optional.get();
//        } else {
//            throw new RuntimeException("Medal Not Found For Id :: " + id);
//        }
    }

    @Override
    public void deleteMedalById(Long id) {
        this.medalRepository.deleteById(id);
    }

    @Override
    public List<Medal> getMedalByCurrencyId(Long currencyId) {
        List<Medal> medals = medalRepository.getMedalByCurrencyId(currencyId);
        return medals;
    }

    @MeasureTime(value = "Update medal in DB",
            color = MeasureTime.ConsoleColor.GREEN)
    @Override
    @Transactional
    public void updateMedal(Medal medal) {
        medalRepository.updateMedal(medal);
    }

    @Override
    public List<CountryByStatus> getCountryByStatus(String status) {
//        List<CountryByStatus> countryByStatusList = new ArrayList<>();

        if (currentUserService.isAdmin()) {
            return medalRepository.countryByStatus(status, null);
        } else {
           return medalRepository.countryByStatus(status, true);
        }
    }

    @Override
    public List<CurrencyByStatus> getCurrencyByStatus(Long countryId, String status) {
        validationService.validateCountry(countryId);
        List<CurrencyByStatus> result;

        if (currentUserService.isAdmin()) {
            result = medalRepository.currencyByStatus(status, countryId, null);
        } else {
            result = medalRepository.currencyByStatus(status, countryId, true);
            if (result.isEmpty()) {
                throw new CountryNotFoundException(countryId);
            }
        }

        return result;
    }

    @Override
    public Page<Medal> findMedalPaginated(Integer pageNo, Integer pageSize, Long currencyId, String status) {
        List<Medal> medals = new ArrayList<>();
        if (currentUserService.isAdmin()) {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.medalRepository.medalPageable(currencyId, status, null, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            return this.medalRepository.medalPageable(currencyId, status, true, pageable);
        }
    }
}

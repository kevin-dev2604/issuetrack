package com.kevinj.portfolio.issuetrack.process.adapter.out;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaStepRepository;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.process.adapter.out.query.StepQueryRepository;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.application.port.StepPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StepPersistenceAdapter implements StepPort {

    private final StepQueryRepository queryRepository;
    private final JpaStepRepository jpaStepRepository;
    private final UserMapper userMapper;
    private final ProcessAndStepMapper mapper;

    @Override
    public List<StepDomain> getAllStepList(User user, ProcessDomain process) {
        return queryRepository.getAllStep(mapper.toProcessEntity(process, userMapper.toUsersEntity(user)))
                .stream()
                .map(mapper::toStepDomain)
                .toList();
    }

    @Override
    public List<StepDomain> getActiveStepList(User user, ProcessDomain process) {
        return queryRepository.getActiveStep(mapper.toProcessEntity(process, userMapper.toUsersEntity(user)))
                .stream()
                .map(mapper::toStepDomain)
                .toList();
    }

    @Override
    public Optional<StepDomain> getStep(User user, ProcessDomain process, Long stepId) {
        return jpaStepRepository.findByStepIdAndProcess(stepId, mapper.toProcessEntity(process, userMapper.toUsersEntity(user)))
                .filter(step -> step.getIsDeleted() == null || !step.getIsDeleted().equals(YN.Y))
                .map(mapper::toStepDomain);
    }

    @Override
    public Optional<StepDomain> getStepUnscoped(Long processId, Long stepId) {
        return queryRepository.getStepUnscoped(processId, stepId)
                .map(mapper::toStepDomain);
    }

    @Override
    public Optional<StepDomain> getInitialStep(User user, ProcessDomain process) {
        return queryRepository.getInitialStep(mapper.toProcessEntity(process, userMapper.toUsersEntity(user)))
                .map(mapper::toStepDomain);
    }

    @Override
    public Optional<StepDomain> getNextStep(User user, ProcessDomain process, Integer order) {
        return queryRepository.getNextStep(mapper.toProcessEntity(process, userMapper.toUsersEntity(user)), order)
                .map(mapper::toStepDomain);
    }

    @Override
    public void createStep(User user, ProcessDomain process, StepCreateInfo stepCreateInfo) {
        Step step = new Step(null, mapper.toProcessEntity(process, userMapper.toUsersEntity(user)), stepCreateInfo.order(), stepCreateInfo.name(), YN.Y);
        jpaStepRepository.save(step);

    }

    @Override
    public void saveStep(User user, ProcessDomain process, StepDomain step) {
        Process processEntity = mapper.toProcessEntity(process, userMapper.toUsersEntity(user));
        Step stepEntity = mapper.toStepEntity(step, processEntity);
        jpaStepRepository.save(stepEntity);

    }
}

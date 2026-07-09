package com.chaao.appserver.service.Impl.wx;

import com.chaao.appserver.mapper.wx.SetmealMapper;
import com.chaao.appserver.service.SetmealService;
import dto.wx.SetmealDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.wx.SetmealVO;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public List<SetmealVO> getSetmealById(SetmealDTO setmealDTO) {
        // 计算偏移量
        setmealDTO.setStart((setmealDTO.getPage() - 1) * setmealDTO.getPageSize());
        return setmealMapper.getSetmealById(setmealDTO);
    }
}

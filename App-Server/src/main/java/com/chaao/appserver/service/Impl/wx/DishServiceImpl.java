package com.chaao.appserver.service.Impl.wx;

import com.chaao.appserver.mapper.wx.DishMapper;
import com.chaao.appserver.service.DishService;
import dto.wx.DishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vo.wx.DishVO;

import java.util.List;
@Service
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;

    // 删掉 @RequestBody！这里是业务方法，不是接口接收参数
    @Override
    public List<DishVO> dishListByCategoryId( DishDTO dishDTO) {
        // 计算偏移量
        dishDTO.setStart((dishDTO.getPage() - 1) * dishDTO.getPageSize());
        return dishMapper.dishListByCategoryId(dishDTO);
    }
}

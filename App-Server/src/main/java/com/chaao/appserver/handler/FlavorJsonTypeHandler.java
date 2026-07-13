package com.chaao.appserver.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import vo.wx.DishVO;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//自定义 TypeHandler。
// 这样 MyBatis 查出数据后会自动把 JSON 字符串转成 List<Flavor>
public class FlavorJsonTypeHandler extends BaseTypeHandler<List<DishVO.Flavor>> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<DishVO.Flavor> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, MAPPER.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<DishVO.Flavor> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<DishVO.Flavor> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<DishVO.Flavor> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<DishVO.Flavor> parse(String json) {
        try {
            if (json == null || json.isEmpty()) return null;
            return MAPPER.readValue(json, new TypeReference<List<DishVO.Flavor>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON to List<Flavor>", e);
        }
    }
}
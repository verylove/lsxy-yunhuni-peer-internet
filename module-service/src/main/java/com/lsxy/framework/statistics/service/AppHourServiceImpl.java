package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.AppHour;
import com.lsxy.framework.api.statistics.service.AppHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.statistics.dao.AppHourDao;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 消费小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class AppHourServiceImpl extends AbstractService<AppHour> implements AppHourService {
    @Autowired
    AppHourDao appHourDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<AppHour, Serializable> getDao() {
        return appHourDao;
    }

    @Override
    public void hourStatistics(Date date, int hour,String[] select) throws  SQLException{
        String selects = "";
        String groupbys = "";
        String wheres = "";
        for(int i=0;i<select.length;i++){
            if(i==select.length-1){
                groupbys += select[i] ;
            }else {
                groupbys += select[i] + " , ";
            }
            selects += select[i] + " , ";
            wheres += select[i]+"=a."+select[i] +" and ";
        }
        String sql =" insert into db_lsxy_base.tb_base_app_hour("+selects+"dt,hour,sum_on_line,sum_line,sum_app_num,create_time,last_time,deleted,sortno,version) ";
        sql +=" select "+selects+" ? as dt,? as hour, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+wheres+" status=1) as sum_on_line, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+wheres+" status=2) as sum_line, ";
        sql +=" COUNT(1) as sum_app_num ,";
        sql +=" ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        if(StringUtil.isNotEmpty(groupbys)){
            groupbys = " group by "+groupbys;
        }
        sql +=" from db_lsxy_bi_yunhuni.tb_bi_app a "+groupbys;
        update(date, hour, sql);
    }

    private void update(final Date date, final int day, String sql) throws  SQLException{
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                Timestamp sqlDate = new Timestamp(date.getTime());//进行日期的转换
                ps.setObject(1,sqlDate);
                ps.setInt(2,day);
                long times = new Date().getTime();
                Timestamp initDate = new Timestamp(times);
                ps.setObject(3,initDate);
                ps.setObject(4,initDate);
                ps.setObject(5,1);
                ps.setObject(6,times);
                ps.setObject(7,0);
            }
        });
    }
}

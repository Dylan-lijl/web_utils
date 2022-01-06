package pub.carzy.api;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import pub.carzy.util.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据封装类
 *
 * @author macro
 * @date 2019/4/19
 */
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将MyBatis Plus 分页结果转化为通用结果
     */
    public static <T> CommonPage<T> restPage(Page<T> pageResult) {
        CommonPage<T> result = new CommonPage<>();
        // 当前页
        result.setPageNum(Convert.toInt(pageResult.getCurrent()));
        // 一页多少条数据
        result.setPageSize(Convert.toInt(pageResult.getSize()));
        // 总数据数量
        result.setTotal(pageResult.getTotal());
        // 总页数
        result.setTotalPage(Convert.toInt(pageResult.getTotal()/pageResult.getSize()+1));
        // 当前页数据
        result.setList(pageResult.getRecords());
        return result;
    }
    public static <T> CommonPage<T> restPage(IPage<T> pageResult) {
        CommonPage<T> result = new CommonPage<>();
        // 当前页
        result.setPageNum(Convert.toInt(pageResult.getCurrent()));
        // 一页多少条数据
        result.setPageSize(Convert.toInt(pageResult.getSize()));
        // 总数据数量
        result.setTotal(pageResult.getTotal());
        // 总页数
        result.setTotalPage(Convert.toInt(pageResult.getTotal()/pageResult.getSize()+1));
        // 当前页数据
        result.setList(pageResult.getRecords());
        return result;
    }

    /**
     * 转换
     * @param list
     * @param clazz
     * @param <T>
     * @param <O>
     * @return
     */
    public static <T,O> IPage<T> transformClass(IPage<O> list, Class<T> clazz) {
        IPage<T> result = new Page<>();
        List<T> data = new ArrayList<>();
        for (O source:list.getRecords()){
            try {
                T instance = clazz.newInstance();
                BeanUtils.copyProperties(source,instance);
                data.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                ExceptionHandler.throwException("该对象无构造方法");
            }
        }
        result.setRecords(data);
        result.setSize(list.getSize());
        result.setCurrent(list.getCurrent());
        result.setPages(list.getPages());
        result.setTotal(list.getTotal());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

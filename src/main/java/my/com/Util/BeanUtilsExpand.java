package my.com.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class BeanUtilsExpand extends BeanUtils {

    public static <Source, Target> List<Target> copyList(@Nonnull Collection<Source> sourceList,
                                                         @Nonnull Class<Target> targetClass) {
        List<Target> list = new ArrayList<>();
        if (sourceList.isEmpty()) {
            return list;
        }
        try {
            for (Source s : sourceList) {
                Target t = targetClass.newInstance();
                BeanUtils.copyProperties(s, t);
                list.add(t);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("BeanUtilsExpand 实例化 targetClass 异常", e.getMessage());
        }
        return list;
    }


}

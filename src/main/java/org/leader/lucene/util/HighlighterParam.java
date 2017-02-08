package org.leader.lucene.util;

/**
 * 高亮参数对象
 *
 * @author ldh
 * @since 2016-10-09 9:36
 */
public class HighlighterParam {
    /**
     * 是否需要设置高亮
     */
    private boolean highlight;
    /**
     * 需要高亮的属性名
     */
    private String fieldName;
    /**
     * 高亮前缀
     */
    private String prefix;
    /**
     * 高亮后缀
     */
    private String suffix;
    /**
     * 显示摘要最大长度
     */
    private int fragmenterLength;

    public HighlighterParam() {
    }

    public HighlighterParam(boolean highlight, String fieldName, String prefix, String suffix, int fragmenterLength) {
        this.highlight = highlight;
        this.fieldName = fieldName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.fragmenterLength = fragmenterLength;
    }

    public HighlighterParam(boolean highlight, String fieldName, int fragmenterLength) {
        this.highlight = highlight;
        this.fieldName = fieldName;
        this.fragmenterLength = fragmenterLength;
    }

    public HighlighterParam(boolean highlight, String fieldName, String prefix, String suffix) {
        this.highlight = highlight;
        this.fieldName = fieldName;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getFragmenterLength() {
        return fragmenterLength;
    }

    public void setFragmenterLength(int fragmenterLength) {
        this.fragmenterLength = fragmenterLength;
    }
}

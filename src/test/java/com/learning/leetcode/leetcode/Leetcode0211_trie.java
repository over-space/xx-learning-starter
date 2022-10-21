package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.Assert;

import java.util.Arrays;

/**
 * 211. 添加与搜索单词 - 数据结构设计
 * 请你设计一个数据结构，支持 添加新单词 和 查找字符串是否与任何先前添加的字符串匹配 。
 *
 * 实现词典类 WordDictionary ：
 *
 * WordDictionary() 初始化词典对象
 * void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
 * bool search(word) 如果数据结构中存在字符串与word 匹配，则返回 true ；否则，返回 false 。word 中可能包含一些 '.' ，每个. 都可以表示任何一个字母。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/design-add-and-search-words-data-structure
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author lifang
 * @since 2021/10/19
 */
public class Leetcode0211_trie extends BaseTest implements Testing{

    @Override
    public void test() {
        WordDictionary wd = new WordDictionary();
        wd.addWord("word");
        wd.addWord("trie");
        wd.addWord("test");
        wd.addWord("testing");
        wd.addWord("text");
        Assert.assertTrue(wd.search("test"));
        Assert.assertTrue(wd.search("test"));
    }

    class WordDictionary {

        private Trie root;

        public WordDictionary() {
            this.root = new Trie();
        }

        public void addWord(String word) {
            this.root.insert(word);
        }

        public boolean search(String word) {
            return search(word, 0, this.root);
        }

        private boolean search(String word, int index, Trie node){
           if(index == word.length()){
               return node.isEnd;
           }
            char ch = word.charAt(index);
            if (Character.isLetter(ch)) {
                // 字母
                int i = ch - 'a';
                Trie child = node.children[i];
                if (child != null && search(word, index + 1, child)) {
                    return true;
                }
            }else{
                for (int i = 0; i < 26; i++) {
                    Trie child = node.children[i];
                    if (child != null && search(word, index + 1, child)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    class Trie{
        private Trie[] children;
        private boolean isEnd;

        public Trie() {
            children = new Trie[26];
            isEnd = false;
        }

        public void insert(String word) {
            Trie node = this;
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                int index = ch - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new Trie();
                }
                node = node.children[index];
            }
            node.isEnd = true;
        }

        public boolean search(String word) {
            Trie trie = startsWithPrefix(word);
            return trie != null && trie.isEnd;
        }

        public boolean startsWith(String prefix) {
            return startsWithPrefix(prefix) != null;
        }

        private Trie startsWithPrefix(String prefix){
            Trie node = this;
            for (int i = 0; i < prefix.length(); i++) {
                char ch = prefix.charAt(i);
                int index = ch - 'a';
                if((node = node.children[index]) == null){
                    return null;
                }
            }
            return node;
        }

        @Override
        public String toString() {
            return "Trie{" +
                    "children=" + Arrays.toString(children) +
                    ", isEnd=" + isEnd +
                    '}';
        }
    }
}

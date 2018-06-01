 #! /bin/sh
    
    #number of directories and files
    DS=0
    FS=0
    #1st param, the dir name
    #2nd param, the aligning space
    function listFiles(){
    	for file in `ls "$1"`
    	do
    		if [ -d "$1/${file}" ];then
    			echo "$2${file}"
    			((DS++))
    			listFiles "$1/${file}" " $2"
    		else
                cd $1
    			echo "${file}"
                mv "${file}" ~/WorkSpace/xiaoqiwang/my_master/btc/t/
    			((FS++))
                cd -
    		fi
    	done	
    	
    }
    
    listFiles $1 "    "
    echo "${DS} dictories,${FS} files"

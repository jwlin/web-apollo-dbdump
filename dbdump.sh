#!/bin/bash

for WEBAPP in agrpla # replace agrpla with one or more species names
do
    if [ -f result/$WEBAPP.txt ]; then rm result/$WEBAPP.txt; fi
    FILEPATH="/app/data/BCM_i5k_pilot/$WEBAPP/jbrowse/annotations"  # replace with your DB path
    echo -e "species\ttype\tid\tname\towner\tseqid\tstart\tend\tstrand\tnote1\tnote2" >> result/$WEBAPP.txt 

    for TYPE in mRNA transcript tRNA snRNA snoRNA ncRNA rRNA miRNA repeat_region transposable_element
    do
        java -cp 'class:lib/*' net.castman.webapollo.DbDump $WEBAPP $TYPE $FILEPATH 2>&1 | tee -a log.txt
    done
done


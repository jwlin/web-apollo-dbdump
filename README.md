# web-apollo-dbdump
Dump user annotations from [Web Apollo](https://github.com/GMOD/Apollo) 1.X database

## Usage
clone the repo
```
git clone https://github.com/jwlin/web-apollo-dbdump.git
```
Modify `WEBAPP` and `FILEPATH` in `dbdump.sh`, and the run
```
$ ./dbdump.sh
Processing agrpla for mRNA
done. 55 records for mRNA
Processing agrpla for transcript
done. 8 records for transcript
Processing agrpla for tRNA
done. 4 records for tRNA
Processing agrpla for snRNA
done. 0 records for snRNA
Processing agrpla for snoRNA
done. 0 records for snoRNA
Processing agrpla for ncRNA
done. 0 records for ncRNA
Processing agrpla for rRNA
done. 0 records for rRNA
Processing agrpla for miRNA
done. 0 records for miRNA
Processing agrpla for repeat_region
done. 0 records for repeat_region
Processing agrpla for transposable_element
done. 0 records for transposable_element
```

See the results under `result`
```
species	type	id	name	owner	seqid	start	end	strand	note1	note2
agrpla	mRNA	EE1AA3C36D07BDC6F5239D597D9CD09E	AplaTmpM000212-RA	user@user.com	Scaffold1	979119	997302	1		
agrpla	mRNA	36ACDA3AC49198A04FBCEB2AB1B0F7EC	Glycoside hydrolase family 28 (partial)	user@user.com	Scaffold109	547045	548162	-1		Annotation type: Add a new gene model not existing in gene setData source for annotation: Similarities to adjacent genes part of the same family.Note to curator:  This gene model is truncated at its 3'-end due to the presence of a gap at this locus.
[.. trimmed]
agrpla	transcript	7AD107A2D3DED7910657B1C1E169ABBD	snap_masked-Scaffold20-abinit-gene-6.6-mRNA-1	user@user.com	Scaffold20	632682	639039	1		
agrpla	transcript	0665A29B16D642FFBFCF80C428A67075	snap_masked-Scaffold20-abinit-gene-6.6-mRNA-1	user@user.com	Scaffold20	632682	639039	1		
[.. trimmed]
agrpla	tRNA	CC3DAD6CEA1F73575DE43D9A37060179	HelroP125059:Helobdella_robusta	user@user.com	Scaffold172	299138	299384	-1		
agrpla	tRNA	23F8A4632547B65CC2B411C92FA0F500	HelroP125059:Helobdella_robusta	user@user.com	Scaffold172	299138	299384	-1
```

## Prerequisite
- Java runtime
- Bourne shell

(Tested on CentOS 6.5)

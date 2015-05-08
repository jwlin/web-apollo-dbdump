package net.castman.webapollo;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

import org.bbop.apollo.web.datastore.JEDatabase;
import org.gmod.gbol.bioObject.AbstractSingleLocationBioFeature;
import org.gmod.gbol.bioObject.SequenceAlteration;
import org.gmod.gbol.simpleObject.Feature;
import org.gmod.gbol.simpleObject.FeatureProperty;
import org.gmod.gbol.simpleObject.FeatureLocation;
import org.gmod.gbol.simpleObject.FeatureRelationship;

import org.apache.tools.ant.DirectoryScanner;

public class DbDump {
	public static void main(String [] args) throws IOException {

        if (args.length != 3) {
            System.out.println("usage: java net.castman.webapollo.DbDump [species] [feature_type] [file_path]");
            System.exit(0);
        }
        String species = args[0];
        String feature_type = args[1];
        String file_path = args[2];

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{file_path + "/Annotations-*"});
        scanner.setExcludes(new String[]{file_path + "/Annotations-*_history"});
		scanner.setCaseSensitive(false);
        scanner.scan();
        String[] dirs = scanner.getIncludedDirectories();

        if (dirs.length == 0) {
            System.out.println("can not find annotation directories of " + species);
            System.exit(0);
        }

        PrintWriter out = new PrintWriter(new FileWriter("result/" + species + ".txt", true));    // append
        //out.println("species: " + species + "\tfeature_type: " + feature_type);
        //out.println("species\tname\towner\tseqid\tstart\tend\tstrand\tnote1\tnote2");
        out.close();

        System.out.println("Processing " + species + " for " + feature_type);
        int count = 0;
        for (String dir : dirs) {
            // ex. dir[0] = /app/data/modencode/Drosophila_takahashii/jbrowse/annotations/Annotations-scf7180000410307
            String[] tokens = dir.split("/");
            String seqid = tokens[tokens.length-1].replace("Annotations-", "");
            //System.out.println(species + "-" + seqid);
            
            File fp = new File(dir);
            if ( (!fp.exists()) || (!fp.isDirectory()) ) {
                System.out.println("seqid " + seqid + ": invalid path.");
                continue;
            }

            // check if .jdb files exist in filePath
            DirectoryScanner scr = new DirectoryScanner();
            scr.setBasedir(dir);
            scr.setIncludes(new String[] {"*.jdb"});
            scr.scan();
            String[] jdbs = scr.getIncludedFiles();
            if (jdbs.length <= 0) {
                System.out.println("seqid " + seqid + ":no jdb files.");
                continue;
            }

            JEDatabase db = new JEDatabase(dir, true);  // readonly = true
            List<Feature> features = new ArrayList<Feature>();
            db.readFeatures(features);
            db.close();
          
            //System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", "species", "name", "owner", "seqid", "start", "end", "strand", "note1", "note2");
            for (Feature feature : features) {

                String name = "";
                String id = "";
                String owner = "";
                int start = 0;
                int end = 0;
                String note1 = "";
                String note2 = "";
                int strand = 0;
                 
				Iterator<FeatureLocation> iter = feature.getFeatureLocations().iterator();
				while (iter.hasNext()) {
					FeatureLocation fLoc = iter.next();
                    start = fLoc.getFmin();
                    end = fLoc.getFmax();
                    strand = fLoc.getStrand();
                }

				Iterator<FeatureProperty> iter2 = feature.getFeatureProperties().iterator();
				while (iter2.hasNext()) {
					FeatureProperty fProp = iter2.next();
                    if (fProp.getType().toString().matches("(.*)comment")) {
                        note1 += fProp.getValue();
                    }
                    if (fProp.getType().toString().matches("(.*)owner")) {
                        owner = fProp.getValue();
                    }
				}					
                
                if (feature.getType().toString().matches("(.*)" + feature_type)) {
                    name = feature.getName();
                    id = feature.getUniqueName();
                    String result = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%d\t%d\t%d\t%s\t%s", species, feature_type, id, name, owner, seqid, start, end, strand, note1, note2);
                    out = new PrintWriter(new FileWriter("result/" + species + ".txt", true)); // append
                    out.println(result);
                    out.close();
                    count++;
                }
                else {
                    Set<FeatureRelationship> childFeatures = feature.getChildFeatureRelationships();
                    if (childFeatures.size() > 0) {
                        for (FeatureRelationship relation : childFeatures) {
                            if (relation.getSubjectFeature().getType().toString().matches("(.*)" + feature_type)) {
                                Feature childFeature = relation.getSubjectFeature();
                                name = childFeature.getName();
                                id = childFeature.getUniqueName();
				                Iterator<FeatureProperty> iter3 = childFeature.getFeatureProperties().iterator();
                                while (iter3.hasNext()) {
                                    FeatureProperty fProp3 = iter3.next();
                                    if (fProp3.getType().toString().matches("(.*)comment")) {
                                        note2 += fProp3.getValue();
                                    }
                                }                                
                                String result = String.format("%s\t%s\t%s\t%s\t%s\t%s\t%d\t%d\t%d\t%s\t%s", species, feature_type, id, name, owner, seqid, start, end, strand, note1, note2);
                                out = new PrintWriter(new FileWriter("result/" + species + ".txt", true)); // append
                                out.println(result);
                                out.close();
                                count++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("done. " + count + " records for " + feature_type);
	}
}

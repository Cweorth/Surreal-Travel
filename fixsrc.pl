#!/usr/bin/perl

#=============================================================================#
#                                                                             #
#  Source code checker                                                        #
#  -------------------                                                        #
#                                                                             #
#  Roman Lacko [2014-12-18], CVT FI MU                                        #
#       xlacko1@fi.muni.cz                                                    #
#                                                                             #
#  Checks files in the current working directory for                          #
#     - missing EOL at the end of file                                        #
#     - CRLF line endings instead of LF                                       #
#     - redundant whitespaces at the end of line                              #
#                                                                             #
#=============================================================================#

use strict;
use warnings;
use Cwd;
use File::Basename;
use File::Copy   qw(copy move);
use File::Remove qw(remove);
use File::Temp   qw(tempfile);
use Getopt::Long;
use IO::Interactive qw(is_interactive);
use List::Util   qw(reduce min);
use Switch;

#-----------------------------------------------------------------------------#
#  Help                                                                       #
#-----------------------------------------------------------------------------#

sub help {
    print("usage: ", basename($0), " [options]\n");
    print("\n");
    print("Checks:\n");
    print("    -a  --all         fix all\n");
    print("    -e  --eol         fix missing EOL at the end of file\n");
    print("    -c  --crlf        fix CRLF (yes, that's an error) :D\n");
    print("    -w  --ws          fix redundant whitespaces at line ends\n");
    print("\n");
    print("Filters:\n");
    print("    --ignore-path  s  list of paths to ignore (ie. \"./.git\")\n");
    print("    --ignore-file  s  list of files to ignore (ie. \"*.png\")\n");
    print("    --ignore-regex s  list of regex to ignore (ie. \"[0-9]+.png\")\n");
    print("\n");
    print("Script configuration:\n");
    print("        --colour   s  colour mode (auto, always, never)\n");
    print("        --color    s  'murican for --colour\n");
    print("    -v  --verbose     increase script verbosity\n");
    print("        --level    l  set verbosity level [0-4]\n");
    print("    -r  --ro          read-only mode (use with at least one -v)\n");
    print("    -b  --backup      create file backup (default), suffix ~\n");
    print("        --nobackup    do not create backups\n");
    print("        --rmbackup    remove backup files\n");
    print("        --revert      revert backup files\n");
    print("    -q  --quiet       do not output [0-3] messages\n");
    print("\n");
    print("Help:\n");
    print("        --help        print this gibberish\n");
}

#-----------------------------------------------------------------------------#
#  Configuration and options                                                  #
#-----------------------------------------------------------------------------#

# Script configuration
my %config = ( 
    colour      => "auto",
    verbose     => 0,
    backup      => 1,
);

# Extensions and paths to ignore
my  @path_ignore = ();
my  @file_ignore = ("*~", basename($0));
my @regex_ignore = (".*~[0-9]+");

# Parse CLI options
GetOptions(
    "all"            => \$config{all},
    "eol"            => \$config{eol},
    "crlf"           => \$config{crlf},
    "ws"             => \$config{ws},

    "colour|color=s" => \$config{colour},
    "ignore-path=s"  => \@path_ignore,
    "ignore-file=s"  => \@file_ignore,
    "ignore-regex=s" => \@regex_ignore,

    "verbose+"       => \$config{verbose},
    "level=i"        => \$config{verbose},
    "ro"             => \$config{ro},
    "backup!"        => \$config{backup},
    "rmbackup"       => \$config{rmbackup},
    "revert"         => \$config{revert},
    "quiet"          => \$config{quiet},
    
    "help"           => sub { help; exit(0); },
) or die("Invalid options: $!, use --help");

#-----------------------------------------------#
# Check verbosity level                         #
#-----------------------------------------------#
sub vlvl {
    my $r = shift;
    return $config{verbose} >= $r;
}

#-----------------------------------------------#
# File backup                                   #
#-----------------------------------------------#
sub mkbackup {
    my $filename = shift;
    my $bckpname = "$filename~";
    my $ix       = "";
    debug("mkbackup", "$filename");

    if (-f $bckpname) {
        debug("mkbackup", col("default", "YEL"), " backup exists");
        my $index = 1;
        for ( ; -f "$bckpname$ix"; ++$ix) {
            debug("mkbackup", "backup ", col("$ix", "YEL"), " exists");
        }
    }

    copy("$filename", "$bckpname$ix");
    return $ix;
}

sub rmbackup {
    my $filename = shift;
    my $bckpname = "$filename~";
    my $ix       = "";
    debug("rmbackup", "$filename");

    debug("rmbackup", "removing ", col("default", "YEL"), " backup") if (-f $bckpname);
    remove(0, "$bckpname");

    for ($ix = 1; -f "$bckpname$ix"; ++$ix) {
        debug("rmbackup", "removing backup ", col("$ix", "YEL"));
        remove(0, "$bckpname$ix");
    }
    return $ix;
}

#-----------------------------------------------#
# Color output                                  #
#-----------------------------------------------#
sub col {
    my $text   = shift;
    my $colour = shift;

    return $text if (!$config{colour});

    switch ($colour) {
        case ("yel") { return   "\e[33m$text\e[39m";    }
        case ("blu") { return   "\e[34m$text\e[39m";    }
        case ("wht") { return   "\e[37m$text\e[39m";    }
        case ("RED") { return "\e[1;31m$text\e[21;39m"; }
        case ("GRN") { return "\e[1;32m$text\e[21;39m"; }
        case ("YEL") { return "\e[1;33m$text\e[21;39m"; }
        case ("BLU") { return "\e[1;34m$text\e[21;39m"; }
        case ("WHT") { return "\e[1;37m$text\e[21;39m"; }

        else         { return $text; }
    }
}

#-----------------------------------------------#
#  Debug                                        #
#-----------------------------------------------#
sub debug {
    return unless vlvl(4);

    my $module = shift;
    print STDOUT (col("DEBUG", "WHT"), ": ", col($module, "BLU"), ": ", join("", @_), "\n");
}

#-----------------------------------------------#
#  Fix skeleton                                 #
#-----------------------------------------------#
# +$  name    name of fix procedure             #
# +$  file    filename                          #
# +$  filter  called as system("$filter $file") #
# +\% fixed   hashref to "fixed" structure      #
# +\S fixsub  procedure to run on each line     #
#-----------------------------------------------#
sub fix {
    my ($name, $file, $filter, $fixed, $fixsub) = @_; 
    my $ret = system("$filter $file");

    if ($ret == 0) {
        my ($fh, $fn) = tempfile(SUFFIX => ".tmp", CLEANUP => 1);
        debug("fix-$name", "temporary file $fn, number ", fileno($fh));

        if ($config{ro} || !open(ORIGINAL, "$file")) {
            my $reason = $config{ro} ? "read-only" : $!;
            debug("fix-$name", col("failed to open", "RED"), " $file: $reason");
            $fixed->{$name} = -1;
        } else {
            while (my $line = <ORIGINAL>) {
                $fixsub->(\$line);
                print $fh ($line);
            }
            close(ORIGINAL);
            close($fh);
            
            my $status = move($fn, $file);
            debug("fix-$name", $status ? col("fixed", "GRN") : (col("fix failed", "RED") . "$?"));
            $fixed->{$name} = 1;
        }
    }

}

#-----------------------------------------------------------------------------#
#  Main                                                                       #
#-----------------------------------------------------------------------------#

# Config colour
$config{colour} = ($config{colour} eq "auto")
                ? is_interactive()
                : ($config{colour} eq "always");

# Get list of files
my $command;
if (@path_ignore + @file_ignore + @regex_ignore > 0) {
    my $part_path  = join(" -o ", map { "-path  \"$_\"" } ( @path_ignore)) || ""; 
    my $part_file  = join(" -o ", map { "-name  \"$_\"" } ( @file_ignore)) || "";
    my $part_regex = join(" -o ", map { "-regex \"$_\"" } (@regex_ignore)) || "";

    my $ignore = reduce { (length($a) > 0 && length($b) > 0) ? "$a -o $b" : "$a$b" } ("", $part_path, $part_file, $part_regex);

    $command = "find . \\( $ignore \\) -prune -o -type f -print";
} else {
    $command = "find . -type f -print";
}

debug("main", "the search command is ", $command);

if ($config{quiet}) {
    debug("main", "quiet option is set");

    open(NULL, ">/dev/null") or die("Cannot open /dev/null!");
    select(NULL);
}

my @files   = `$command`;
map { chomp($_) } @files;

if (($? >> 8) != 0) {
    debug("main", col("failed to execude find", "RED"), ": $!");
    print (col("ERROR", "RED"), ": failed to execute find: $!\n");
    die("$!");
}

#==[  Main cycle  ]===========================================================#
debug("main", "PWD is ", getcwd());
print("Checking " . col(getcwd(), "BLU") . "\n");

#--[  Fixes  ]----------------------------------------------------------------#
if ($config{all} || $config{eol} || $config{crlf} || $config{ws}) {
    my $allcounter = 0;
    my $counter    = 0;

    print("Fixing problems in files...\n");
    debug("main", "read-only mode enabled") if ($config{ro});
    debug("main", "backups are enabled")    if ($config{backup});

    foreach my $file (@files) {
        debug("main", "found file $file");
        ++$allcounter;
        my %fixed;
        mkbackup($file) if ($config{backup} && !$config{ro});

        #--[  EOL fix  ]------------------------------------------------------#
        if ($config{all} || $config{eol}) {
            my $ret = system("tail -c1 $file | read -r _");

            if ($ret != 0) {
                my $status = $config{ro} ? 1           : system("echo >> $file") >> 8;
                my $reason = $config{ro} ? "read-only" : "return code " . $status;
                debug("fix-eol", $status ? (col("fix failed", "RED") . ": $reason") : col("fixed", "GRN"));
                $fixed{eol} = $status ? -1 : 1;
            }
        }

        #--[  CRLF fix  ]-----------------------------------------------------#
        if ($config{all} || $config{crlf}) {
            fix("crlf", $file, "fgrep -Uq \"\r\"", \%fixed, sub { my $x = shift; $$x =~ s/\r\n/\n/g; });
        }

        #--[  WS fix  ]-------------------------------------------------------#
        if ($config{all} || $config{ws}) {
            fix("ws", $file, "egrep -q \"\\s+\$\"", \%fixed, sub { my $x = shift; $$x =~ s/\s+\n/\n/g; });
        }

        #--[  Print output  ]-------------------------------------------------#
    
        if (keys(%fixed) == 0) {
            print("    [ " . col( "OK" , "GRN") . " ] (---) $file\n") if vlvl(3);
        
            rmbackup($file) if (!$config{ro});
        } else {
            ++$counter;

            if (vlvl(1)) {
                my $min = min (values(%fixed));
                print("    [" . (($min < 0) ? col("FAIL", "RED") : col("FIXD", "YEL")) . "] ");
                if (vlvl(2)) {
                    print("(");
                    print(exists $fixed{eol}  ? col("e", $fixed{eol}  < 0 ? "RED" : "YEL") : "-");
                    print(exists $fixed{crlf} ? col("c", $fixed{crlf} < 0 ? "RED" : "YEL") : "-");
                    print(exists $fixed{ws}   ? col("w", $fixed{ws}   < 0 ? "RED" : "YEL") : "-");
                    print(") ");
                }
                print("$file\n");
            }
            rmbackup($file) if (!$config{backup} && !$config{ro});
        }
    }

    print("found files: " . col("$allcounter", "WHT") . "\n");
    print("fixed files: " . col("$counter",    "YEL") . "\n");
}

#--[  Revert and remove  ]-----------------------------------------------------#

if ($config{revert} || $config{rmbackup}) {
    debug("main", $config{revert} ? "reverting" : "removing", " backups");
    print(        $config{revert} ? "Reverting" : "Removing", " backups...\n");
    my $all = 0;
    my $rev = 0;

    foreach my $file (@files) {
        ++$all;

        debug("rev&rm", "found file $file");
        if (-f "$file~") {
            debug("rev&rm", col("backup found", "GRN"));
            my $status = $config{ro} 
                       ? 0 
                       : ($config{revert} ?     move("$file~", "$file") 
                                          : rmbackup("$file"));

            my $reason = $config{ro} ? "read-only" : $!;
            if ($status) {
                debug("rev&rm", col("success", "GRN"));
                print("    [ ", col("OK", "GRN"), " ] $file\n") if vlvl(2);
                ++$rev;
            } else {
                debug("rev&rm", col("failure", "RED"), ": $reason");
                print("    [", col("FAIL", "RED"), "] $file\n") if vlvl(1);
                print("    >>>>>> $reason\n") if vlvl(1);               
            }
        } else {
            debug("rev&rm", col("no backup found", "YEL"));
            print("    [", col("E404", "YEL"), "] $file\n") if vlvl(3);
        }
    }

    print("found files:       " . col("$all", "WHT") . "\n");
    print("processed backups: " . col("$rev", "YEL") . "\n");
}

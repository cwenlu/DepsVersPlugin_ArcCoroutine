

//def isNonStable = { ComponentSelection selection ->
//    boolean rejected = ['release', 'final', 'alpha', 'beta', 'rc', 'ga'].any { qualifier ->
//        selection.candidate.version ==~ /(?i).*[.-]${qualifier}[.\d-]*/
//    }
//    return rejected
//}
//
//tasks.named("dependencyUpdates").configure {
//    resolutionStrategy {
//        componentSelection {
//            all { ComponentSelection selection ->
//
//                if (isNonStable(selection)) {
//                    selection.reject('Release candidate')
//                }
//
//            }
//        }
//    }
//}


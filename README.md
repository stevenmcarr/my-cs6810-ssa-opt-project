# my-cs6810-ssa-opt-project 
## Stats 

Here are my stats for each file in `./input/`. Only `dyanmic.il` and `newdyn.il` in optimized form required spill code when allocating with 16 registers.

| File | Original # of Insts. | Optimized # of Insts. | Percent Reduced (Orig - Opt/Orig)| Reg. Alloc # Inst. | Final % Reduced |
| - | - | - | - | - | - | 
|`arrayparam.il`  |841    |443    |47.3% | 443     | 47.3% |
|`bubble.il`      |4374   |2481   |43.3% | 2481    | 43.3% |
|`check.il`       |140    |3      |97.9% | 3       | 97.9% |
** |`dynamic.il`     |39155  |18434  |52.9% | 19311   | 50.7% | **
|`fib.il`         |274    |211    |23%   | 211     | 23% |
|`gcd.il`         |103    |73     |29.1% | 73      | 29.1% |
** |`newdyn.il`      |136919 |64270  |53.1% | 65204   | 52.4% | **
|`qs.il`          |4574   |3073   |32.8% | 3073    | 32.8% |
|`while_array.il` |377    |255    |32.4% | 255     | 32.4% |

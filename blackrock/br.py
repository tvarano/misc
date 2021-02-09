def read(filename): 
    f = File.open(filename)
    lines = f.readlines()

    ret = [len(lines)]

# trait, trait, 
# name, value,
 
    trs = []
    for h in lines[0].split(","):
        trs.append(h)

    # while file has lines: 
        # ret.append({})
    for i in range(len(lines)): 
        data = l.split(",") 
        ret[i] = {}
        for t in trs: 
            ret[i][t] = data

    return ret



class MyMap: 
    
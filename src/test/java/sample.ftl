[#ftl]



[#macro examDatestable examDates]


<table id="exam-dates-table">
    <thead>
    <tr class="odd">
        <th>Day</th>
        <th>Date</th>
        <th>Extra Info</th>
    </tr>
    </thead>

    <tbody>
    [#assign dateChecked=false]
    [#list examDates as examDate]
      [#if examDate.getDeskAvailable() != true]
        [#assign rowClass='disable']
        [#assign radioAttrs='disabled']
      [#else]
        [#assign rowClass='enable']
        [#assign radioAttrs='']
      [/#if]

      [#assign dateCheckedAttr='']
        [#if rowClass != 'disable' && dateChecked != true ]
          [#assign dateCheckedAttr='checked="checked"']
          [#assign dateChecked=true]
          [#assign rowClass=rowClass + ' selected']
        [/#if]


    <tr class='${rowClass!""}'>
        <td>
            <input type="radio" value="${examDate.date?string('yyyy/MM/dd')}" ${dateCheckedAttr} ${radioAttrs}> ${examDate.date?string("EEEE")}
        </td>
        <td>${examDate.date?string("MMM dd, yyyy")}</td>
        <td>[#if examDate.extraFee]'Extra Fee Charged'[/#if]</td>
    </tr>

    [/#list]
    [#assign rowClass=""]
  

    </tbody>
</table>

[/#macro]



[@examDatestable examDates/]
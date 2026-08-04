> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Navigation Editor and Remove Alignment Blueprint

## Row layout

```kotlin
@Composable
fun NavigationEditorRow(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    visible: Boolean,
    mandatory: Boolean,
    removable: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
    dragHandle: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Switch(
                checked = visible,
                onCheckedChange = if (mandatory) null else onVisibilityChange,
            )
        }

        if (removable) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(
                        R.string.remove_main_navigation_shortcut,
                        title,
                    ),
                )
            }
        } else {
            Box(Modifier.size(48.dp))
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            dragHandle()
        }
    }
}
```

## Why this fixes the old defect

- every action occupies the same 48dp slot;
- all slots share center alignment;
- text height cannot shift actions;
- no custom bottom padding on remove;
- static/dynamic rows preserve column geometry.

## Accessibility

Drag handle should expose:

- move up;
- move down;
- current position.

Remove button names the shortcut.

## Picker

Use a lazy list in a Material bottom sheet. Do not place all options in an unbounded Column.
